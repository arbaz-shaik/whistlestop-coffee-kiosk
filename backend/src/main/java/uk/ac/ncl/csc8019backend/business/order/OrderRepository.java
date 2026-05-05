package uk.ac.ncl.csc8019backend.business.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.ncl.csc8019backend.business.common.OrderStatus;


public interface OrderRepository extends JpaRepository<Order , Long> {
    
// Methods Arbaz's StaffService need me here 

// Active order sorted by pickup time - for staff dashboard
// "findBy" + "ArchivedFalse" + "OrderBy" + "PickupTime" + "Asc"

List<Order> findByArchivedFalseOrderByPickupTimeAsc();

// filter active order by status to show pending orders 
// "findBy" + "Status" + "And" + "ArchivedFalse"

List<Order> findByStatusAndArchivedFalse(OrderStatus status); 

// filter active order by status sorted by pickup time
// Arbaz call this in getOrderByStatus

List<Order>findByStatusAndArchivedFalseOrderByPickupTimeAsc(OrderStatus status);

// Archived Orders sorted by most recently updated - for order history
// "findBy" + "ArchivedTrue" + "OrderBy" + "UpdatedAt" + "Desc"

List<Order> findByArchivedTrueOrderByUpdatedAtDesc();

// Methods you need for OrderService 
// save(order)=already provided free by JpaRepository — INSERT or UPDATE
// findById(id)=already provided free by JpaRepository — find by primary key

Long countByStatus(OrderStatus status);

// Method for orderService 

// save order - free from JpaRepository - INSERT Or UPdATE 
// findById  - free fro JpaRepository find by primary key 
}

    

