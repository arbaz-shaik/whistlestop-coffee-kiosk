package uk.ac.ncl.csc8019backend.business.common;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message){
        super(message);

    }
}
