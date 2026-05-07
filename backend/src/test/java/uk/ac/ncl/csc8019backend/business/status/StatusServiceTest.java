package uk.ac.ncl.csc8019backend.business.status;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.Order;
import uk.ac.ncl.csc8019backend.business.order.OrderRepository;


/** 
Unit tests for the StatusService.
 * 
 * Mocks OrderRepository and ApplicationEventPublisher with Mockito,
 * so these tests verify business logic in isolation without touching 
* the database or even publishing Spring events.
 * 
 * Testing in three fields:
 updateOrderStatus(): valid/invalid transitions, missing orders
* canTransition(): the rules of the state-machine
* cancelOrder(): cancel from various states
* 
* @author Shaik Arbaz *
*/

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private StatusService statusService;
    
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = new Order();
        sampleOrder.setStatus(OrderStatus.PENDING);
    }

    //updateOrderStatus() tests

    @Test
    void updateOrderStatus_validTransition_updatesAndPublishesEvent(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        Order result = statusService.updateOrderStatus(1L,OrderStatus.ACCEPTED);

        assertEquals(OrderStatus.ACCEPTED, result.getStatus());
        assertNotNull(result.getUpdatedAt());
        verify(orderRepository).save(sampleOrder);
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }
    
    @Test 
    void updateOrderStatus_invalidtransition_throwsException(){
        sampleOrder.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

        assertThrows(IllegalStateException.class,
            () -> statusService.updateOrderStatus(1L,OrderStatus.READY));

            verify(orderRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
    }
    @Test
    void updateOrderStatus_orderNotFound_throwsException(){
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> statusService.updateOrderStatus(99L, OrderStatus.ACCEPTED));
    }

    //can transition() tests, covering valid and invalid transitions based  rules

    @Test 
    void canTrasintion_pendingToAccepted_true(){
        assertTrue(statusService.canTransition(OrderStatus.PENDING, OrderStatus.ACCEPTED));
    }

    @Test 
    void canTransition_acceptedToInProgress_true(){
        assertTrue(statusService.canTransition(OrderStatus.ACCEPTED, OrderStatus.IN_PROGRESS));
    }

    @Test 
    void canTransition_inProgressToReady_true() {
        assertTrue(statusService.canTransition(OrderStatus.IN_PROGRESS, OrderStatus.READY));
    }

    @Test 
    void canTransition_readyToCollected_true() {
        assertTrue(statusService.canTransition(OrderStatus.READY, OrderStatus.COLLECTED));
    }

    @Test 
    void canTransition_anyToCancelled_true() {
        assertTrue(statusService.canTransition(OrderStatus.PENDING,OrderStatus.CANCELLED));
        assertTrue(statusService.canTransition(OrderStatus.ACCEPTED, OrderStatus.CANCELLED));
        assertTrue(statusService.canTransition(OrderStatus.IN_PROGRESS, OrderStatus.CANCELLED));
        assertTrue(statusService.canTransition(OrderStatus.READY, OrderStatus.CANCELLED));
    }

    @Test 
    void canTransition_skippingState_false() {
        assertFalse(statusService.canTransition(OrderStatus.PENDING, OrderStatus.READY));
        assertFalse(statusService.canTransition(OrderStatus.PENDING, OrderStatus.COLLECTED));
        assertFalse(statusService.canTransition(OrderStatus.ACCEPTED, OrderStatus.COLLECTED));     
    }

    @Test 

    void canTransition_fromTerminalState_false() {
        assertFalse(statusService.canTransition(OrderStatus.COLLECTED, OrderStatus.PENDING));
        assertFalse(statusService.canTransition(OrderStatus.CANCELLED, OrderStatus.PENDING));
        assertFalse(statusService.canTransition(OrderStatus.COLLECTED, OrderStatus.CANCELLED));
        assertFalse(statusService.canTransition(OrderStatus.CANCELLED, OrderStatus.CANCELLED));
    }

    // cancelOrder() tests

    @Test 
    void cancelOrder_pendingOrder_cancels() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);
        Order result = statusService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test 
    void cancelOrder_alreadyCancelled_throwsException() {
        sampleOrder.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

        assertThrows(IllegalStateException.class,
            () -> statusService.cancelOrder(1L));
    }

    @Test 
    void cancelOrder_collected_throwsException() {
        sampleOrder.setStatus(OrderStatus.COLLECTED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

        assertThrows(IllegalStateException.class,
            () -> statusService.cancelOrder(1L));
    }
}

