package uk.ac.ncl.csc8019backend.business.payment.dto;

import java.math.BigDecimal;
/**
 * DRO maps the paymetn section of the checkuot request
 * once the card details a paseed to HorsePay only last 4 digit will be saved and all other will be discarded
 
 */

public class PaymentRequest{

    private String paymentMethod;
    private BigDecimal amount;
    private String cardNumber;
    private String cardholderName;
    private String cvv;
    private String expiryDate;

    public PaymentRequest(){}


        public String getPaymentMethod(){
            return paymentMethod;
        }

        public BigDecimal getAmount(){
            return amount;
        }
        //return last 4 numbers

        public String getCardLastFour(){
            if (cardNumber ==null||cardNumber.length()<4){
                return null;
            }
            return cardNumber.substring(cardNumber.length()-4);
        }

        public String getCardholderName(){
            return cardholderName;
        }

        public String getCardNumber(){
            return cardNumber;
        }
        
        public String getCvv(){
            return cvv;
        }
        public String getExpiryDate(){
            return expiryDate;
        }

        public void setPaymentMethod(String paymentMethod){
            this.paymentMethod = paymentMethod;
        }

        public void setAmount(BigDecimal amount){
            this.amount = amount;
        }

        public void setCardholderName(String cardholderName){
            this.cardholderName = cardholderName;
        }

        public void setCvv(String cvv){
            this.cvv = cvv;
        }

        public void setExpiryDate(String expireDate){
            this.expiryDate = expireDate;
        }

        public void setCardNumber(String cardNumber){
            this.cardNumber = cardNumber;
        }

        



}