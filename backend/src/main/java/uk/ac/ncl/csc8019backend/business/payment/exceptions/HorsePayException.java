 package uk.ac.ncl.csc8019backend.business.payment.exceptions;
 public class HorsePayException extends RuntimeException{
/**
 * Throwmn when HorsePay API returns an unexpected or API is unreachable.
 */
    public HorsePayException(String message){
        super(message);
    }
    public HorsePayException(String message, Throwable cause) {
        super(message,cause);
    }
 }