package uk.ac.ncl.csc8019backend.business.common;

/**
 * Represents the lifecycle status of an order in the Whistlestop Coffee system.
 * Orders progress through these states from placement to collection.
 * 
 * @author Shaik Arbaz
 */
public enum OrderStatus {
    /**
     * Order has been placed by customer, awaiting staff acceptance.
     */
    PENDING,
    
    /**
     * Staff has accepted the order and will begin preparation.
     */
    ACCEPTED,
    
    /**
     * Staff is currently preparing the order.
     */
    IN_PROGRESS,
    
    /**
     * Order is ready for customer collection at the kiosk.
     */
    READY,
    
    /**
     * Customer has collected their order. Terminal state.
     */
    COLLECTED,
    
    /**
     * Order has been cancelled (no-show or out of stock). Terminal state.
     */
    CANCELLED
}