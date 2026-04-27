package uk.ac.ncl.csc8019backend.business.status;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ncl.csc8019backend.buisness.order.Order;
import uk.ac.ncl.csc8019backend.buisness.order.OrderRepository;
import uk.ac.ncl.csc8019backend.buisness.common.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;


/**
 * the order status is managed using combination of :
 * 1. Map-based validation : it define what are the allowed transitions
 * 2. Event-driven architecture: this broadcast the status changes and listers react to it independently
 * 
 * This design follows:
 * Open/close: no featues can be added via listenters, the class is not modified
 * Single Responsibility: this validates and updates the status
 * @author Shaik Arbaz
 */

@Service

public class StatusService{

}

    
