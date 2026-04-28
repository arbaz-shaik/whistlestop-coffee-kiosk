package uk.ac.ncl.csc8019backend.business.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;

/**
 * Database access layer for Order entity
 * the spring has readymade database operations, you just need to define a method name it and it generate SQL
 */

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{

    // for StaffServie: count orders by status
    long countByStatus(OrderStatus  status);

    // For StaffService : to get orders by status (Active orders)
    List<Order> findByStatusAndArchivedFalseOrderByPickupTimeAsc(OrderStatus status);

    // For StaffServices: to get archived orders
    List<Order> findByArchivedTrueOrderByUpdatedAtDesc();

    //for StaffService: get all active orders (non archived)
    List<Order> findByArchivedFalseOrderByPickupTimeAsc();

    //for AutoCancellationTaskt
    List<Order> findByStatusAndPickupTimeBefore(OrderStatus status, LocalDateTime cutoff);

}
