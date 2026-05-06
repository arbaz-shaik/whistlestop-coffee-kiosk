package uk.ac.ncl.csc8019backend.business.status;


import  java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.Order;
import uk.ac.ncl.csc8019backend.business.order.OrderRepository;

/**
 * pending order after their pick up time has pass will be auto cancelled after 15mins
 * Scheduled tast runs every5mins to check and autocancel the order
 * 
 */

@Component
public class AutoCancellationTask{

    private final StatusService statusService;
    private final OrderRepository orderRepository;

    public AutoCancellationTask(OrderRepository orderRepository, StatusService statusService){
        this.orderRepository = orderRepository;
        this.statusService = statusService;
    }



    @Scheduled(fixedRate = 300000 )//300,000  ms, ie 5minutes
    public void cancelOverdueOrders(){
        List<Order> overdueOrders = orderRepository.findByStatusAndPickupTimeBefore(
            OrderStatus.PENDING, LocalDateTime.now()
        );
        for (Order order : overdueOrders){
            statusService.cancelOrder(order.getId());
        }

}

}