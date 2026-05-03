package uk.ac.ncl.csc8019backend.business.order;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderRequest {
    private String customerName;
    private String customerEmail;       // Optional
    private String customerPhone;       // Optional
    private LocalDateTime pickupTime;         // ISO 8601 format e.g. "2024-06-01T14:30:00"
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {}

    // getters
    public String getCustomerName() { 
        return customerName; 
    }
    public String getCustomerEmail() { 
        return customerEmail; 
    }
    public String getCustomerPhone() { 
        return customerPhone; 
    }
    public LocalDateTime getPickupTime() { 
        return pickupTime; 
    }
    public List<OrderItemRequest> getItems() { 
        return items; 
    }

     // setters
     public void setCustomerName(String customerName) {
         this.customerName = customerName;
     }
     public void setCustomerEmail(String customerEmail) {
         this.customerEmail = customerEmail;
     }
     public void setCustomerPhone(String customerPhone) {
         this.customerPhone = customerPhone;
     }
     public void setPickupTime(LocalDateTime pickupTime) {
         this.pickupTime = pickupTime;
     }
     public void setItems(List<OrderItemRequest> items) {
         this.items = items;
     }
    
}
