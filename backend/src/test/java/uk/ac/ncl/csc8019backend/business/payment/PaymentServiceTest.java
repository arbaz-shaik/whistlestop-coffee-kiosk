package uk.ac.ncl.csc8019backend.business.payment;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.ac.ncl.csc8019backend.business.payment.dto.HorsePayResponse;
import uk.ac.ncl.csc8019backend.business.payment.dto.PaymentRequest;
import uk.ac.ncl.csc8019backend.business.payment.dto.PaymentResponse;
import uk.ac.ncl.csc8019backend.business.payment.exceptions.PaymentFailedException;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    HorsePayClient horsePayClient;

    @InjectMocks
    PaymentService paymentService;

    private PaymentRequest request;

    @BeforeEach
    void init() {

        request = new PaymentRequest();

        request.setPaymentMethod("CARD");
        request.setCardNumber("4242424242424242");
        request.setCardholderName("Jane Smith");
        request.setExpiryDate("12/25");
        request.setCvv("123");

        BigDecimal amount = new BigDecimal("10.99");
        request.setAmount(amount);
    }

    @Test
    void paymentShouldBeSuccessful() {

        BigDecimal amount = new BigDecimal("10.99");

        Payment payment =
                new Payment(1L, amount, "CARD");

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        HorsePayResponse response =
                new HorsePayResponse();

        HorsePayResponse.PaymentStatus paymentStatus =
                new HorsePayResponse.PaymentStatus();

        paymentStatus.setStatus(true);
        paymentStatus.setReason("payment successful");

        response.setPaymetSuccess(paymentStatus);

        when(horsePayClient.processPayment(
                anyString(),
                any(BigDecimal.class)
        )).thenReturn(response);

        PaymentResponse result =
                paymentService.processPayment(
                        1L,
                        "CF3838",
                        request
                );

        assertTrue(result.isSuccess());

        assertEquals(
                "payment successful",
                result.getMessage()
        );

        assertEquals(
                "4242",
                result.getCardLastFour()
        );

        assertEquals(amount, result.getAmount());

        verify(paymentRepository,
                times(2)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenBankRejectsPayment() {

        Payment p =
                new Payment(
                        1L,
                        new BigDecimal("10.99"),
                        "CARD"
                );

        when(paymentRepository.save(any()))
                .thenReturn(p);

        HorsePayResponse horseResponse =
                new HorsePayResponse();

        HorsePayResponse.PaymentStatus status =
                new HorsePayResponse.PaymentStatus();

        status.setStatus(false);
        status.setReason("transaction declined by bank");

        horseResponse.setPaymetSuccess(status);

        when(horsePayClient.processPayment(
                anyString(),
                any(BigDecimal.class)
        )).thenReturn(horseResponse);

        PaymentFailedException ex =
                assertThrows(
                        PaymentFailedException.class,
                        () -> paymentService.processPayment(
                                1L,
                                "CF3838",
                                request
                        )
                );

        assertEquals(
                "transaction declined by bank",
                ex.getMessage()
        );
    }
}