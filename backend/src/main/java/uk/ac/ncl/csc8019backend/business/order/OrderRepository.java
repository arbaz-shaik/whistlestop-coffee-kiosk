package uk.ac.ncl.csc8019backend.business.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;

public interface OrderRepository extends JpaRepository<Order , Long>{
    
    List<Order> findByArchivedFalseOrderByPickupTimeAsc();


    List<Order> findByStatusAndArchivedFalse(OrderStatus status);
    
    List<Order> findByStatusAndArchivedFalseOrderByPickupTimeAsc(OrderStatus status);

    List<Order> findByArchivedTrueOrderByUpdatedAtDesc();

    Long countByStatus(OrderStatus status);

    List<Order> findByStatusAndPickupTimeBefore(OrderStatus status,LocalDateTime time);
}
