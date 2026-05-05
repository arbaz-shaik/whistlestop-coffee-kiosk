package uk.ac.ncl.csc8019backend.business.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import uk.ac.ncl.csc8019backend.business.common.ItemUnavailableException;
import uk.ac.ncl.csc8019backend.business.common.OrderNotFoundException;
import uk.ac.ncl.csc8019backend.business.menu.MenuItem;
import uk.ac.ncl.csc8019backend.business.menu.MenuService;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuService menuService;

    // Spring injects both dependencies automatically

    public OrderService(OrderRepository orderRepository, MenuService menuService){
        this.orderRepository = orderRepository;
        this.menuService = menuService;
    }
    // create a new order.
    // step 1 : validate pickup time 
    // step 2 : validate customer name
    // step 3 : validate all items exist and are available 
    // step 4 : build order item with snapshot price
    // step 5 : create and save order

    public Order createOrder(CreateOrderRequest request){
        
        // Step 1 : validate pickup time
        // OpeningHoursUtil throws InvalidPickupTimeException if invalid 
        OpeningHoursUtil.validate(request.getPickupTime());

        // step 2 : validate customer name
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        // step 3 and 4 : validate all item and build order items 
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.getItems()) {

            // validate quantity 
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            // validate size
            String size = itemRequest.getSize();
            if (!"regular".equalsIgnoreCase(size) && !"large".equalsIgnoreCase(size)) {
                throw new IllegalArgumentException("Size must be 'regular' or 'large'");
            }

            // Look up the menu item - throws ItemNotFoundException if not found.
            MenuItem menuItem = (MenuItem) menuService.getMenuItemById(itemRequest.getMenuItemId());
            if (!menuItem.getAvailable()) {
                throw new ItemUnavailableException(menuItem.getName() + " is currently unavailable");
            }

            // check large size is valid for this item
            // getPriceForSize throws IllegalArgumentException if large size is not available
            BigDecimal price = menuItem.getPriceForSize(size);

            // Build order item with snapshot price
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSize(size);
            orderItem.setPrice(price);

            orderItems.add(orderItem);

        }
        // step 5 : Create and save order
        Order order = new Order(
            request.getCustomerName(),
            request.getCustomerEmail(),
            request.getCustomerPhone(),
            request.getPickupTime()
        );

        // link each item to this order and add to the list 
        for (OrderItem item : orderItems) {
            order.addItem(item);  // addItem() keeps both sides on sync 
        }
        // save cascade saves all order item too 
        return orderRepository.save(order);
    }
   
    // Retrieves an order by ID
    // called by get /api/orders/{id}
    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with id: " + id
                    ));
    }
    
}


