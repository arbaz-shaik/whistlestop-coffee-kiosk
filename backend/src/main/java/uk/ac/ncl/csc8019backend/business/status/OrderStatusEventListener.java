package  uk.ac.ncl.csc8019backend.business.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * listens for the status change
 * logs the status change
 * 
 * @author Shaik Arbaz
 */

@Component

public class OrderStatusEventListener{
    private static final Logger logger = LoggerFactory.getLogger(OrderStatusEventListener.class);
    /**
     * whenver an OrderStausChangedEvent, this will invoked automatically
     * logs the changes at INFO level
     * 
     * @param event the status change event carrtying old and new status
     */

    @EventListener
    public void handleStatusChange(OrderStatusChangedEvent event){
        logger.info("Status cahnged: {}", event );

    }
}