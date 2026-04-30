package uk.ac.ncl.csc8019backend.business.staff;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.Order;
import uk.ac.ncl.csc8019backend.business.order.OrderRepository;

/**
 *  Staff dasboard operations servics.
 * Offers read-only quesries for statistics and ative/archived orders
 * 
 * @author Shaik Arbaz
 */



@Service
public class StaffService{

/**
 * @param orderRepository Repository for database operations
 */

    private final OrderRepository orderRepository;


    public StaffService( OrderRepository orderRepository){
        this.orderRepository = orderRepository;
  


       
    }

    /** 
     *  Returns list of non-Archived order srted by pick up time
    */

    public List<Order> getAllActiveOrders(){
       return orderRepository.findByArchivedFalseOrderByPickupTimeAsc();

    }

    /**
     * List of  orders are returned arranged by pickup time and filted by status
     */

    public List<Order> getOrdersByStatus(OrderStatus status){
        return orderRepository.findByStatusAndArchivedFalseOrderByPickupTimeAsc(status);

    }

    /**
     * Retrives a single order by its ID or raise an exception if not found
     */

    public Order getOrderById(Long orderId){

        return orderRepository.findById(orderId)
        .orElseThrow(()-> new IllegalArgumentException(
            "Order not found with Id :" + orderId
        ));


    }

    /**
     * Archives an order. Only if COLLECTED or CANCELLED orders can be archived
     */

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

    /**
     * returns List of all Archived orders
     */

    public List<Order> getArchivedOrders(){
        return orderRepository.findByArchivedTrueOrderByUpdatedAtDesc();
        }

    /**
     * Returns a count of orders grouped by each status
     * 
     */

    public Map<OrderStatus, Long> getOrderStatistics(){

        Map<OrderStatus, Long> stats = new HashMap<>();
        for (OrderStatus status: OrderStatus.values()){
            stats.put(status, orderRepository.countByStatus(status));
        }
        return stats;
    }




}