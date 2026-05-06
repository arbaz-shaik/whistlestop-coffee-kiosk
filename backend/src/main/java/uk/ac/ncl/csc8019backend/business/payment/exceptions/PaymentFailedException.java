 package uk.ac.ncl.csc8019backend.business.payment.exceptions;
 public class PaymentFailedException extends RuntimeException {
/**
 * when the HorsePay API declines the payment this Exception is thrown;
 */
    public PaymentFailedException(String message) {
        super(message);
    }
 }