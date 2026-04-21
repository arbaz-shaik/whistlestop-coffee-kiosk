package uk.ac.ncl.csc8019backend.business.status;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.csc8019backend.business.common.OrderStatus;

/**
 * Service layer for managing order status transitions and lifecycle.
 * Handles status updates, validation, and auto-cancellation.
 * 
 * @author Shaik Arbaz
 */
@Service
public class StatusService {

    /**
     * Updates the status of an order after validating the transition.
     * 
     * @param orderId the ID of the order to update
     * @param newStatus the new status to set
     * @throws IllegalArgumentException if the status transition is invalid
     */
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        // TODO: Implement in next phase when Order entity exists
        // Implementation will include:
        // 1. Fetch order from repository
        // 2. Validate transition using canTransition()
        // 3. Update order.status and order.updatedAt
        // 4. Save to database
    }

    /**
     * Validates whether a status transition is allowed.
     * 
     * Valid transitions:
     * - PENDING → ACCEPTED or CANCELLED
     * - ACCEPTED → IN_PROGRESS or CANCELLED
     * - IN_PROGRESS → READY or CANCELLED
     * - READY → COLLECTED or CANCELLED
     * - COLLECTED → (no transitions allowed - terminal state)
     * - CANCELLED → (no transitions allowed - terminal state)
     * 
     * @param current the current status
     * @param next the desired next status
     * @return true if transition is valid, false otherwise
     */
    public boolean canTransition(OrderStatus current, OrderStatus next) {
        // Terminal states cannot transition
        if (current == OrderStatus.COLLECTED || current == OrderStatus.CANCELLED) {
            return false;
        }

        // Can always cancel (unless already in terminal state)
        if (next == OrderStatus.CANCELLED) {
            return true;
        }

        // Valid forward transitions
        return switch (current) {
            case PENDING -> next == OrderStatus.ACCEPTED;
            case ACCEPTED -> next == OrderStatus.IN_PROGRESS;
            case IN_PROGRESS -> next == OrderStatus.READY;
            case READY -> next == OrderStatus.COLLECTED;
            default -> false;
        };
    }

    /**
     * Cancels an order by setting status to CANCELLED.
     * 
     * @param orderId the ID of the order to cancel
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // TODO: Implement when Order entity exists
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    /**
     * Scheduled task to auto-cancel orders not collected 15 minutes after pickup time.
     * This method should be triggered by a scheduler (e.g., @Scheduled annotation).
     * 
     * TODO: Add @Scheduled annotation when ready to activate
     */
    public void checkOverdueOrders() {
        // TODO: Implement auto-cancellation logic
        // 1. Find orders with status=READY and pickupTime + 15min < now
        // 2. For each, call cancelOrder()
    }
}