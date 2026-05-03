package uk.ac.ncl.csc8019backend.business.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * represent a payment recored in payment table i.e database
 * Traks the HorsePay response and payment details (last four card digits)
 * @author Shaik Arbaz
 */

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "horse_pay_reference_id", length = 100)
    private String horsePayReferenceId;

    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;

    @Column(name = "horse_pay_reason", length = 255)
    private String horsePayReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(length = 20, nullable = false)
    private String status = "PENDING";

// when a new payment is made the tmestamps sets automatically
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
// when payment is modified the time stamps update automatically
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    
    public Payment() {}

    /**
     * Creates new payment with PENDING status
     * @param orderId : the payment belongs t this order
     * @param paymentMethod : CARD or DIGITAL_WALLET
     * @param amount : total amount in gbp
     */
    
    public Payment(Long orderId, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "PENDING";
    }

    public Long getId() {
        return id;
        }


    public Long getOrderId() {
        return orderId;
        }



    public BigDecimal getAmount() {
        return amount;
        }


    public String getPaymentMethod() {
        return paymentMethod;
        }

    public String getStatus() {
        return status;
        }


    public String getHorsePayReferenceId() {
        return horsePayReferenceId;
        }


    public String getCardLastFour() {
        return cardLastFour;
        }
 
    public String getHorsePayReason() {
        return horsePayReason;
        }
  

    public LocalDateTime getCreatedAt() {
        return createdAt;
        }
 
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
        }

    public void setId(Long id) {
            this.id = id;
            }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
        }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        }

    public void setStatus(String status) {
        this.status = status;
        }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;}
    
    public void setHorsePayReferenceId(String ref) {
        this.horsePayReferenceId = ref;
        }

       public void setCardLastFour(String cardlastFour) {
        this.cardLastFour = cardLastFour;
        }

    public void setHorsePayReason(String reason) {
        this.horsePayReason = reason;
        }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        }


}