package uk.ac.ncl.csc8019backend.business.payment;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import uk.ac.ncl.csc8019backend.business.payment.dto.PaymentRequest;

public class PaymentRequestTest {

    @Test
    void cardLastFour_normalCard() {
        PaymentRequest r = new PaymentRequest();
        r.setCardNumber("4242424242424242");
        assertEquals("4242", r.getCardLastFour());
    }

    @Test
    void cardLastFour_tooShort_returnsNull() {
        PaymentRequest r = new PaymentRequest();
        r.setCardNumber("123");
        assertNull(r.getCardLastFour());
    }

    @Test
    void cardLastFour_nullCardNumber() {
        PaymentRequest r = new PaymentRequest();
        // no card number set at all
        assertNull(r.getCardLastFour());
    }

    @Test
    void allGettersAndSetters() {
        PaymentRequest req = new PaymentRequest();
        req.setPaymentMethod("CARD");
        req.setCardNumber("4242424242424242");
        req.setCardholderName("Jane Smith");
        req.setExpiryDate("12/25");
        req.setCvv("123");
        req.setAmount(new BigDecimal("10.99"));

        assertEquals("CARD", req.getPaymentMethod());
        assertEquals("4242424242424242", req.getCardNumber());
        assertEquals("Jane Smith", req.getCardholderName());
        assertEquals("12/25", req.getExpiryDate());
        assertEquals("123", req.getCvv());
        assertEquals(new BigDecimal("10.99"), req.getAmount());
    }
}
