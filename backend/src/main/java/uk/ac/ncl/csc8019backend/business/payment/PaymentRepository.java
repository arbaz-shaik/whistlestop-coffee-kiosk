package uk.ac.ncl.csc8019backend.business.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * At startup the Spring data JPA reads the method names and auto generates
 *JpaRepository<Paytment,Long>
 * payment: the etity this repo manages
 * long : primaykey Data typt
 * by extent to KpaRepository we get save(), findById(), findAll(), delete()
 @author Shaik Arbaz
 */

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    /**
     * using optional as payment for order might not have done yet
     * this avoid risking NullPointer Exception by forcing caller to handle "not found" explicitly
     * 
     */
    Optional<Payment>findByOrderId(Long orderId);
}



