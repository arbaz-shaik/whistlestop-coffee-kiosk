package uk.ac.ncl.csc8019backend.business.staff;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.csc8019backend.business.common.OrderStatus;

import java.util.List;
import java.util.Map;

/**
 * Service layer for staff dashboard operations.
 * Provides APIs to view, filter, and manage orders for staff members.
 * 
 * @author Shaik Arbaz
 */
@Service
public class StaffService {

    /**
     * Retrieves all active (non-archived) orders sorted by pickup time.
     * 
     * @return list of active orders
     */
    public List<Object> getAllActiveOrders() {
        // TODO: Implement when Order entity and repository exist
        // Query: SELECT * FROM orders WHERE archived = false ORDER BY pickup_time ASC
        return List.of();
    }

    /**
     * Retrieves active orders filtered by specific status.
     * 
     * @param status the status to filter by
     * @return list of orders with given status
     */
    public List<Object> getOrdersByStatus(OrderStatus status) {
        // TODO: Implement when Order entity and repository exist
        // Query: SELECT * FROM orders WHERE status = ? AND archived = false
        return List.of();
    }

    /**
     * Retrieves a single order by ID with full details including items.
     * 
     * @param orderId the order ID
     * @return order with items and customer details
     */
    public Object getOrderById(Long orderId) {
        // TODO: Implement when Order entity and repository exist
        // Query: SELECT * FROM orders WHERE id = ? (with JOIN on order_items)
        return null;
    }

    /**
     * Archives a completed or cancelled order.
     * Only COLLECTED or CANCELLED orders can be archived.
     * 
     * @param orderId the order ID to archive
     * @throws IllegalStateException if order is not in a terminal state
     */
    @Transactional
    public void archiveOrder(Long orderId) {
        // TODO: Implement when Order entity exists
        // 1. Fetch order
        // 2. Validate status is COLLECTED or CANCELLED
        // 3. Set archived = true
        // 4. Save
    }

    /**
     * Retrieves archived orders for history view, sorted by most recently updated.
     * 
     * @return list of archived orders
     */
    public List<Object> getArchivedOrders() {
        // TODO: Implement when Order entity and repository exist
        // Query: SELECT * FROM orders WHERE archived = true ORDER BY updated_at DESC
        return List.of();
    }

    /**
     * Gets count of orders by status for dashboard statistics.
     * Used to display badge counts on staff dashboard.
     * 
     * @return map of status to count (e.g., {PENDING: 5, ACCEPTED: 3})
     */
    public Map<OrderStatus, Long> getOrderStatistics() {
        // TODO: Implement when Order entity and repository exist
        // Group active orders by status and count
        return Map.of();
    }

    /**
     * Retrieves orders that are approaching or past their pickup time.
     * Useful for highlighting urgent orders in the staff dashboard.
     * 
     * @return list of orders needing immediate attention
     */
    public List<Object> getUrgentOrders() {
        // TODO: Implement when Order entity exists
        // Find orders where pickup_time is within next 15 minutes
        // and status is IN_PROGRESS or READY
        return List.of();
    }
}