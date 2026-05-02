package uk.ac.ncl.csc8019backend.business.status;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ncl.csc8019backend.business.order.Order;
import uk.ac.ncl.csc8019backend.business.order.OrderRepository;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;


/**
 * the order status is managed using combination of :
 * 1. Map-based validation : it define what are the allowed transitions
 * 2. Event-driven architecture: this broadcast the status changes and listers react to it independently
 * 
 * This design follows:
 * Open/close: no featues can be added via listenters, the class is not modified
 * Single Responsibility: this validates and updates the status
 * @author Shaik Arbaz
 */

@Service
public class StatusService{

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    /**
     * Contstructor with Dependency Injection
     * OrderRepository instance is provided by the Springboot
     * 
     * @param orderRepository is Repository used for database Operations 
     */

    public StatusService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher){
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;

    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus){
        // Fetch the order
        Order order = orderRepository.findById(orderId)
        .orElseThrow(()-> new IllegalArgumentException(
            "Order not found with Id :" + orderId
        ));
        OrderStatus oldStatus = order.getStatus();

        // Validate transition
        if (!canTransition(order.getStatus(),newStatus)){
            throw new IllegalStateException(
                String.format("Invalid status Transition : from %s to %s is not allowed for order%d", order.getStatus(), newStatus, orderId)
            );

        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        // Save and return

        Order updatedOrder = orderRepository.save(order);

        OrderStatusChangedEvent event = new OrderStatusChangedEvent(orderId, oldStatus, newStatus);
        eventPublisher.publishEvent(event);
        return updatedOrder;

    }

    /**
     * Validate whether a status tansiton is allowed 
     * 
     * state  machine rules:
     * - PENDING -> ACCEPTED OR CANCELLED
     * -ACCEPTED -> IN_PROGRESS OR CANCELLED
     * -READY -> COLLECTED OR CANCELLED
     * Terminal state cannot transition
     * 
     * @param current : current state
     * @param next : desired next state
     * @return true if transition is valid, false otherwise
    
     */

    public boolean canTransition(OrderStatus current, OrderStatus next){
        // Terminal states cannot transition
        if (current == OrderStatus.CANCELLED || current == OrderStatus.COLLECTED){
 
        return false;
        }
        //can cancel from any non-terminal state

        if (next == OrderStatus.CANCELLED){
            return true;
        }

        // Valid forwar Transitions

        return switch(current){
            case PENDING -> next == OrderStatus.ACCEPTED;
            case ACCEPTED -> next == OrderStatus.IN_PROGRESS;
            case IN_PROGRESS-> next == OrderStatus.READY;
            case  READY -> next == OrderStatus.COLLECTED;
            default -> false;


        };

       


    }

     /**
         * Cancels and order by setting status to CANCLLED
         * 
         * @param orderId the ID of the order to cancel
         * @return the cancelled Order entity
         */

    @Transactional
    public Order cancelOrder(Long orderId){
        return updateOrderStatus(orderId,OrderStatus.CANCELLED);
    }

}

