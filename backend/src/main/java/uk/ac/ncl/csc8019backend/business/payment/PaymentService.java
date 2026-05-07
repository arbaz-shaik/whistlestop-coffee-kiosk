package uk.ac.ncl.csc8019backend.business.payment;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ncl.csc8019backend.business.payment.dto.HorsePayResponse;
import uk.ac.ncl.csc8019backend.business.payment.dto.PaymentRequest;
import uk.ac.ncl.csc8019backend.business.payment.dto.PaymentResponse;
import uk.ac.ncl.csc8019backend.business.payment.exceptions.PaymentFailedException;


/** 
* Integrates with the HorsePay to process payments for orders
* payment gateway.  Creates Payment record, calls HorsePay and update
* the record associated with the answer.
* HorsePay is processed atomically as part of the order creation: HorsePay
* declines the transaction, a PaymentFailedException is thrown to indicate that the
* caller can rollback order creation.
* @author Team07
*/


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final HorsePayClient horsePayClient;

    public PaymentService(PaymentRepository paymentRepository, HorsePayClient horsePayClient) {
        this.paymentRepository = paymentRepository;
        this.horsePayClient = horsePayClient;
    }

        /** 
    * Makes a payment for the specified order using HorsePay.
    * Saves a Payment record (PENDING), then calls HorsePay with a generated
    * customer id, then sets Payment to PAID or FAILED depending on the
    * gateway reply.
    * 
    * @param orderId database ID 
    * @param orderNumber human-readable orderId
    * @param request payment details including amount and card metadata
    * @return PaymentResponse with the successful payment
    * @throws PaymentFailedException when HorsePay declines the transaction
    */

    @Transactional
    public PaymentResponse processPayment(Long orderId, String orderNumber, PaymentRequest request) {
        Payment payment = new Payment(orderId, request.getAmount(), request.getPaymentMethod());
        payment.setCardLastFour(request.getCardLastFour());
        payment = paymentRepository.save(payment);

        String customerID = "WCK-" + orderNumber + "-" + Instant.now().getEpochSecond();

        HorsePayResponse horsePayResponse = horsePayClient.processPayment(customerID,
                request.getAmount());
        boolean success = horsePayResponse.getPaymetSuccess().isStatus();
        String reason = horsePayResponse.getPaymetSuccess().getReason();

        payment.setHorsePayReferenceId(customerID);
        payment.setHorsePayReason(reason);

        if (success) {
            payment.setStatus("PAID");
            paymentRepository.save(payment);
            return new PaymentResponse(true, reason, request.getCardLastFour(), request.getAmount());
        } else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new PaymentFailedException(reason);
        }
    }
}
