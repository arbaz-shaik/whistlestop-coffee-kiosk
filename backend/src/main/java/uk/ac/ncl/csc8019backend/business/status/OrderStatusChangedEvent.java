package uk.ac.ncl.csc8019backend.business.status;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;
import java.time.LocalDateTime;

/**
 * whenever the order status is changed an event is published
 * and the status update message broadcasted to the all listeners
 * the Event carries the Status change 
 * and what to do with that change is listeners job
 * @author Shaik Arbaz
 */

public class OrderStatusChangedEvent{

    private final LocalDateTime timestamp;
    private final Long orderId;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;

    public OrderStatusChangedEvent(Long orderId, OrderStatus oldStatus, OrderStatus newStatus){

        this.orderId = orderId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.timestamp = LocalDateTime.now();

    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public Long getOrderId(){
        return orderId;
    }

    public OrderStatus getOldStatus(){
        return oldStatus;
    }

    public OrderStatus getNewStatus(){
        return newStatus;
    }

    @Override 
    public String toString(){
        return String.format("Order #%d: %s to %s at %s",
        orderId, oldStatus, newStatus, timestamp
        );
    }




}


