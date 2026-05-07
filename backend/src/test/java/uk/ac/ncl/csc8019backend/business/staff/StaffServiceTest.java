package uk.ac.ncl.csc8019backend.business.staff;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.Order;
import uk.ac.ncl.csc8019backend.business.order.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    StaffService staffService;

    Order sample;

    @BeforeEach
    void setup() {
        sample = new Order();
        sample.setStatus(OrderStatus.PENDING);
    }

    @Test
    void getAllActiveOrders_returnsAllOrders() {
        Order a = new Order();
        Order b = new Order();
        when(orderRepository.findByArchivedFalseOrderByPickupTimeAsc())
                .thenReturn(Arrays.asList(a, b));

        List<Order> result = staffService.getAllActiveOrders();
        assertEquals(2, result.size());
    }

    // filter by READY status
    @Test
    void getOrdersByStatus_ready() {
        when(orderRepository.findByStatusAndArchivedFalseOrderByPickupTimeAsc(OrderStatus.READY))
                .thenReturn(List.of(sample));

        List<Order> res = staffService.getOrdersByStatus(OrderStatus.READY);

        assertEquals(1, res.size());
        verify(orderRepository).findByStatusAndArchivedFalseOrderByPickupTimeAsc(OrderStatus.READY);
    }

    @Test
    void getOrderById_works() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sample));
        Order o = staffService.getOrderById(1L);
        assertEquals(sample, o);
    }

    @Test
    void getOrderById_missing_throws() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> staffService.getOrderById(99L));
    }

    @Test
    void archiveCollectedOrder() {
        sample.setStatus(OrderStatus.COLLECTED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(orderRepository.save(any())).thenReturn(sample);

        Order result = staffService.archiveOrder(1L);

        assertTrue(result.getArchived());
        verify(orderRepository).save(sample);
    }

    @Test
    void archiveCancelledOrder() {
        sample.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(orderRepository.save(any())).thenReturn(sample);

        Order r = staffService.archiveOrder(1L);
        assertTrue(r.getArchived());
    }

    @Test
    void cantArchivePendingOrder() {
        // PENDING orders are still active so this should fail
        sample.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sample));

        assertThrows(IllegalStateException.class,
                () -> staffService.archiveOrder(1L));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void cantArchiveInProgress() {
        sample.setStatus(OrderStatus.IN_PROGRESS);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sample));
        assertThrows(IllegalStateException.class, () -> staffService.archiveOrder(1L));
    }

    @Test
    void getArchivedOrders_returnsArchived() {
        Order x = new Order();
        when(orderRepository.findByArchivedTrueOrderByUpdatedAtDesc()).thenReturn(List.of(x));

        List<Order> archived = staffService.getArchivedOrders();
        assertEquals(1, archived.size());
    }

    @Test
    void statistics_hasEntryForEachStatus() {
        when(orderRepository.countByStatus(any(OrderStatus.class))).thenReturn(2L);

        Map<OrderStatus, Long> stats = staffService.getOrderStatistics();

        // should have one entry per OrderStatus value
        assertEquals(OrderStatus.values().length, stats.size());
        assertEquals(2L, stats.get(OrderStatus.PENDING));
    }
}