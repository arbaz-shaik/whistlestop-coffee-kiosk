```java
package uk.ac.ncl.csc8019backend.business.staff;

import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.Order;
import uk.ac.ncl.csc8019backend.business.order.OrderRepository;



@Service
public class StaffService{


    private final OrderRepository orderRepository;


    public StaffService( OrderRepository orderRepository){
        this.orderRepository = orderRepository;
  


       
    }

    public List<Order> getAllActiveOrders(){
       return orderRepository.findByArchivedFalseOrderByPickupTimeAsc();

    }

    public List<Order> getOrdersByStatus(OrderStatus status){
        return orderRepository.findByStatusAndArchivedFalseOrderByPickupTimeAsc(status);

    }

    public Order getOrderById(Long orderId){

        return orderRepository.findById(orderId)
        .orElseThrow(()-> new IllegalArgumentException(
            "Order not found with Id :" + orderId
        ));


    }

    public Order archiveOrder(Long orderId){
        Order order = getOrderById(orderId);
    
        if(order.getStatus() == OrderStatus.COLLECTED || order.getStatus() == OrderStatus.CANCELLED){

            order.setArchived(true);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
            return order;

        } 

        throw new IllegalStateException("Order is still active cannot be Archived");

    }

    public List<Order> getArchivedOrders(){
        return orderRepository.findByArchivedTrueOrderByUpdatedAtDesc();
        }

    public Map<OrderStatus, Long> getOrderStatistics() {
    Map<OrderStatus, Long> stats = new HashMap<>();

    for (OrderStatus status : OrderStatus.values()) {
        stats.put(status, orderRepository.countByStatus(status));
    }

    return stats;
}




}
```