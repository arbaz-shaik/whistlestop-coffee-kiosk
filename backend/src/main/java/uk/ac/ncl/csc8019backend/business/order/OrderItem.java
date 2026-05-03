package uk.ac.ncl.csc8019backend.business.order;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import uk.ac.ncl.csc8019backend.business.menu.MenuItem;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // many order item belong to the one order
    // we only store the foreign key (order_id column ) not the full order object yet
    // this gets set when Order is created
    //@auther parthbhilare 

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    // many order item can reference one menu item 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String size;

    // snapshot price - price at the time of ordering
    // if menu price changes later, this order still shows that customer actually paid 

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // constructors 

    public OrderItem() {
    }

    public OrderItem(Order order, MenuItem menuItem, Integer quantity, String size, BigDecimal price) {
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
    }

    //getters

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

    //setters
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

    // total cost for this line item e.g. 2 x latte = 2 x £3.00 = £6.00
    public BigDecimal getLineTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
