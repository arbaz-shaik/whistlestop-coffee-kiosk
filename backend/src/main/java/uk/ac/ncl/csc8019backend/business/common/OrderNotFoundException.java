package uk.ac.ncl.csc8019backend.business.common;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message){
        super(message);
        }
}