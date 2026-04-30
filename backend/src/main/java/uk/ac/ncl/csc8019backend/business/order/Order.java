package uk.ac.ncl.csc8019backend.business.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import uk.ac.ncl.csc8019backend.business.common.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Customer details ──────────────────────────────────────────────────────

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;       // Optional

    @Column(name = "customer_phone")
    private String customerPhone;       // Optional

    // ── Order details ─────────────────────────────────────────────────────────

    @Column(name = "pickup_time", nullable = false)
    private LocalDateTime pickupTime;

    // Arbaz's enum — we set to PENDING, Arbaz's StatusService updates it later
    @Enumerated(EnumType.STRING)        // Stores "PENDING" in DB, not a number
    @Column(nullable = false)
    private OrderStatus status;

    // Arbaz's StaffService uses this to archive completed orders
    // We set to false, Arbaz modifies it later
    @Column(nullable = false)
    private Boolean archived = false;

    // ── Timestamps ────────────────────────────────────────────────────────────

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Arbaz's StatusService updates this when status changes
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ── Order items ───────────────────────────────────────────────────────────

    // One order has many order items
    // mappedBy = "order" means OrderItem.order field owns the relationship
    // cascade = ALL means: if you save/delete an Order, its items go too
    // orphanRemoval = true means: if you remove an item from the list, delete it from DB
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // ── Lifecycle hooks ───────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;   // Always starts as PENDING
        this.archived = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Order() {}

    public Order(String customerName, String customerEmail,
                 String customerPhone, LocalDateTime pickupTime) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.pickupTime = pickupTime;
    }

    // ── Getters
    public Long getId() {
         return id; 
        }

    public String getCustomerName(){
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
    
    public OrderStatus getStatus() { 
        return status; 
        }
    
    public Boolean getArchived() { 
        return archived; 
        }
    
    public LocalDateTime getCreatedAt() {
         return createdAt; 
        }

    public LocalDateTime getUpdatedAt() {
         return updatedAt;
         }
    

    public List<OrderItem> getItems() { 
        return items; 
    }

    // setter

    public void setCustomerName(String customerName){
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

public void setStatus(OrderStatus status) { 
        this.status = status;
       }

public void setArchived(Boolean archived) { 
    this.archived = archived; 
       }

public void setUpdatedAt(LocalDateTime updatedAt) {
     this.updatedAt = updatedAt;  
       }

public void setItems(List<OrderItem> items) { 
    this.items = items; 
       }
    

    // ── Helper method 
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);   // keeps both sides of the relationship in sync
    }
}