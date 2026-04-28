package uk.ac.ncl.csc8019backend.business.order;

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
 main

    //for AutoCancellationTaskt
    List<Order> findByStatusAndPickupTimeBefore(OrderStatus status, LocalDateTime cutoff);

}
