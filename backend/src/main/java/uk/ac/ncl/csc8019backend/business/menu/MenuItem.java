package uk.ac.ncl.csc8019backend.business.menu;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_items")

public class MenuItem {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name ="regular_price", nullable = false ,  precision = 10, scale = 2)
    private BigDecimal regularPrice;

    @Column(name= "large_price", precision = 10, scale = 2)
    private BigDecimal largePrice;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public MenuItem(){}

    public MenuItem(String name, BigDecimal regularPrice, BigDecimal largePrice, Boolean available) {
        this.name = name;
        this.regularPrice = regularPrice;
        this.largePrice = largePrice;
            this.available = available;
        }
        // getter 
        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public BigDecimal getRegularPrice() {
            return regularPrice;
        }
        public BigDecimal getLargePrice() {
            return largePrice;
        }
        public Boolean getAvailable() {
            return available;
        }
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        
        // setter 
        public void setName(String name) {
            this.name = name;
        }
        public void setRegularPrice(BigDecimal regularPrice) {
            this.regularPrice = regularPrice;
        }
        public void setLargePrice(BigDecimal largePrice) {
            this.largePrice = largePrice;
        }
        public void setAvailable(Boolean available) {
            this.available = available;
        }

        public boolean hasLargeOption(){
            return this.largePrice != null;
        }
        public BigDecimal getPriceForSize(String size){
            if ("large".equalsIgnoreCase(size)){
                if(!hasLargeOption()){
                    throw new IllegalArgumentException(name + " is not available in large size.");
                }
                return this.largePrice;
            }
            // default to regular size price
            return this.regularPrice;
        }
    }
    