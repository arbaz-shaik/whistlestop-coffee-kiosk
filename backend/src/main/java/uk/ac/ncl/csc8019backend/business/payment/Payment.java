package uk.ac.ncl.csc8019backend.business.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name ="payments")

public class Payment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false, precision=10, scale =2)
    private BigDecimal amount;

    @Column(lenght = 20, nullable = false)
    private 


}


