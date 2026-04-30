
package uk.ac.ncl.csc8019backend.business.order;

import jakarta.persistence.*;
import uk.ac.ncl.csc8019backend.business.menu.MenuItem;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many order items belong to one order
    // We only store the foreign key (order_id column) — not the full Order object yet
    // This gets set when Order is created
    //author: parthbhilare

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Many order items can reference one menu item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String size;   // "regular" or "large"


    // Snapshot price — price at the time of ordering
    // If menu prices change later, this order still shows what customer actually paid

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal price;

        //constructors
    public OrderItem() {}

    public OrderItem(Order order, MenuItem menuItem, Integer quantity, String size, BigDecimal price) {
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
    }
    // getter 
     public Long getId() {
        return id;
    }
    public Order getOrder() {
        return order;
    }
    public MenuItem getMenuItem() {
        return menuItem;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public String getSize() {
        return size;
    }
    public BigDecimal getPrice() {
        return price;
    }   
//setter

public void setOrder(Order order) {
    this.order = order;
}
public void setMenuItem(MenuItem menuItem) {
    this.menuItem = menuItem;
}
public void setQuantity(Integer quantity) {
    this.quantity = quantity;
}
public void setSize(String size) {
    this.size = size;
}
public void setPrice(BigDecimal price) {
    this.price = price;
}
// Total cost for this line item e.g. 2 x Latte Large = 2 x £3.00 = £6.00
public BigDecimal getLineTotal(){
    return price.multiply(BigDecimal.valueOf(quantity));
}

    }