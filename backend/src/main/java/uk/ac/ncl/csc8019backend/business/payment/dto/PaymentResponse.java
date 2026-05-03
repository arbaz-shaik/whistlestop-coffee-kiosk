package uk.ac.ncl.csc8019backend.business.payment.dto;

import java.math.BigDecimal;

/**
 * Outbound DTO is return t frontend after processing the payment
 * contain only whta screen needs congirmation/error ;
 */

public class PaymentResponse{

    private String message;
    private String cardLastFour;
    private boolean success;
    private BigDecimal amount;

    public PaymentResponse(){}

    public PaymentResponse(boolean success, String message, String cardLastFour, BigDecimal amount){
        this.success = success;
        this.message = message;
        this.cardLastFour = cardLastFour;
        this.amount = amount;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public String getCardLastFour(){
        return cardLastFour;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setCardLastFour(String cardLastFour){
        this.cardLastFour = cardLastFour;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
}