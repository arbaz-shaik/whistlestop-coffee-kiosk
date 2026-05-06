 package uk.ac.ncl.csc8019backend.business.payment;

 import java.time.Instant;

 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import uk.ac.ncl.csc8019backend.business.payment.dto.HorsePayResponse;
 import uk.ac.ncl.csc8019backend.business.payment.dto.PaymentRequest;
 import uk.ac.ncl.csc8019backend.business.payment.dto.PaymentResponse;
import uk.ac.ncl.csc8019backend.business.payment.exceptions.PaymentFailedException;
 
 /**
  * Handles Paymetn processing
  * sends payment request, creates payment records and update the results
  * 
  */


 @Service
 public class PaymentService{
    
    private final PaymentRepository paymentRepository;
    private final HorsePayClient horsePayClient;

    public PaymentService(PaymentRepository paymentRepository,HorsePayClient horsePayClient) {
        this.paymentRepository = paymentRepository;
        this.horsePayClient = horsePayClient;
    }
    /**
     * uses HorsePay gateway to process payment 
     * saves the pending payment record first, then calss Horse.
     * Update 
     * @param orderID : the database ID of of the 
     * @param orderNumber : human resable order number
     * @throws HorsePayException : if HorsePay declines the payment
     * @throws PaymentFailedExpection : if  Horse APU unreachable
     * @return PaymentResponse succces sta
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

        if(success){
            payment.setStatus("PAID");
            paymentRepository.save(payment);
            return new PaymentResponse(true, reason, request.getCardLastFour(), request.getAmount());
        }
        else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new PaymentFailedException(reason);
        }
    }
 }