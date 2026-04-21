# CSC8019 | Whistlestop Coffee Hut | Team 07

## Backend Development: Status & Staff
**By Shaik Arbaz**

---

## 1. Purpose

This document presents a draft individual proposal for my assigned backend development tasks within the Whistlestop Coffee Hut project. My role focuses specifically on the **Status and Staff modules**, the two core components that enable staff to manage order statuses, view incoming orders, and handle order lifecycle from acceptance to collection or cancellation.

This proposal describes my current understanding of the requirements, my approach to module design, and planned structure. It reflects the state of thinking and planning at this early stage and is not a final deliverable.

---

## 2. Background & Context

### 2.1 Project Overview

The Whistlestop Coffee Hut system is a mobile-first web application that enables customers to pre-order drinks from a kiosk at Cramlington Station. The kiosk operates Monday to Friday 06:30–19:00 and Saturday 07:00–18:00, closed Sundays.

The project is divided across six roles. I am assigned to backend role: **Status and Staff**.

### 2.2 My Assigned Scope

- **Status Module** - Managing order status transitions, validation of status changes, and auto-cancellation logic for uncollected orders.
- **Staff Module** - Providing APIs for staff dashboard to view all orders, filter by status, archive completed orders, and retrieve order details.

### 2.3 Integration with Team Member C (Parth)

My modules depend heavily on the Order entity created by Parth. Close coordination is required on:
- Shared OrderStatus enum values
- Database schema for orders table
- Naming conventions and package structure
- Status transition rules

---

| **Document Type** | Draft Individual Proposal |
|-------------------|---------------------------|
| **Module** | CSC8019 |
| **Team** | Shaik Arbaz - Team 07 |
| **Author Role** | Backend Developer: Status & Staff |
| **Technology Stack** | IntelliJ IDEA, Java, Spring Boot, MySQL |
| **Stage** | Design & Initial Implementation |
| **Date** | 21st April 2026 |

---

## 3. Design Approach

### 3.1 General Architecture

Both modules follow the standard Spring Boot layered pattern:
**Entity** → **Repository (JPA)** → **Service** → **(Controller, handled by Team)**

All business logic lives in the service layer inside the agreed directory:
`uk/ac/ncl/csc8019backend/business/`

### 3.2 Status Module Design

The Status module is responsible for managing the order lifecycle through status transitions. 

**Key Responsibilities:**
1. **Update order status** - Change status from one state to another
2. **Validate transitions** - Ensure orders follow correct lifecycle (PENDING → ACCEPTED → IN_PROGRESS → READY → COLLECTED)
3. **Auto-cancellation** - Automatically cancel orders not collected 15 minutes after pickup time
4. **Prevent invalid transitions** - Orders cannot skip states or go backwards

**Status Lifecycle:**

No problem! Here's your complete proposal document:

Create file: docs/proposals/Status_Staff_Module_Shaik_Arbaz.md
Paste this complete content:
markdown# CSC8019 | Whistlestop Coffee Hut | Team 07

## Backend Development: Status & Staff
**By Shaik Arbaz**

---

## 1. Purpose

This document presents a draft individual proposal for my assigned backend development tasks within the Whistlestop Coffee Hut project. My role focuses specifically on the **Status and Staff modules**, the two core components that enable staff to manage order statuses, view incoming orders, and handle order lifecycle from acceptance to collection or cancellation.

This proposal describes my current understanding of the requirements, my approach to module design, and planned structure. It reflects the state of thinking and planning at this early stage and is not a final deliverable.

---

## 2. Background & Context

### 2.1 Project Overview

The Whistlestop Coffee Hut system is a mobile-first web application that enables customers to pre-order drinks from a kiosk at Cramlington Station. The kiosk operates Monday to Friday 06:30–19:00 and Saturday 07:00–18:00, closed Sundays.

The project is divided across six roles. I am assigned to backend role: **Status and Staff**.

### 2.2 My Assigned Scope

- **Status Module** - Managing order status transitions, validation of status changes, and auto-cancellation logic for uncollected orders.
- **Staff Module** - Providing APIs for staff dashboard to view all orders, filter by status, archive completed orders, and retrieve order details.

### 2.3 Integration with Team Member C (Parth)

My modules depend heavily on the Order entity created by Parth. Close coordination is required on:
- Shared OrderStatus enum values
- Database schema for orders table
- Naming conventions and package structure
- Status transition rules

---

| **Document Type** | Draft Individual Proposal |
|-------------------|---------------------------|
| **Module** | CSC8019 |
| **Team** | Shaik Arbaz - Team 07 |
| **Author Role** | Backend Developer: Status & Staff |
| **Technology Stack** | IntelliJ IDEA, Java, Spring Boot, MySQL |
| **Stage** | Design & Initial Implementation |
| **Date** | 21st April 2026 |

---

## 3. Design Approach

### 3.1 General Architecture

Both modules follow the standard Spring Boot layered pattern:
**Entity** → **Repository (JPA)** → **Service** → **(Controller, handled by Team)**

All business logic lives in the service layer inside the agreed directory:
`uk/ac/ncl/csc8019backend/business/`

### 3.2 Status Module Design

The Status module is responsible for managing the order lifecycle through status transitions. 

**Key Responsibilities:**
1. **Update order status** - Change status from one state to another
2. **Validate transitions** - Ensure orders follow correct lifecycle (PENDING → ACCEPTED → IN_PROGRESS → READY → COLLECTED)
3. **Auto-cancellation** - Automatically cancel orders not collected 15 minutes after pickup time
4. **Prevent invalid transitions** - Orders cannot skip states or go backwards

**Status Lifecycle:**
PENDING → ACCEPTED → IN_PROGRESS → READY → COLLECTED
↓          ↓            ↓          ↓
CANCELLED  CANCELLED    CANCELLED  CANCELLED

**Design Decisions:**
- OrderStatus stored as ENUM in database for type safety
- Service layer validates all transitions before persisting
- Auto-cancellation implemented via Spring @Scheduled task (runs every 5 minutes)
- Orders cannot transition backwards (e.g., READY cannot go back to IN_PROGRESS)
- Terminal states (COLLECTED, CANCELLED) cannot be changed

### 3.3 Staff Module Design

The Staff module provides APIs for the staff dashboard to manage orders efficiently.

**Key Responsibilities:**
1. **View all active orders** - Get list of non-archived orders sorted by pickup time
2. **Filter by status** - View only orders with specific status (e.g., only PENDING orders)
3. **View order details** - Get full information for a single order including items and customer info
4. **Archive completed orders** - Move COLLECTED or CANCELLED orders to archive
5. **Dashboard statistics** - Provide counts of orders by status for badges

**Design Decisions:**
- Soft deletion using `archived` boolean flag rather than deleting records
- Orders sorted by pickup time (earliest first) for staff priority
- Only COLLECTED or CANCELLED orders can be archived
- Statistics calculated dynamically from active orders
- All queries filter out archived orders unless specifically requested

---

## 4. Key Components

### 4.1 Status Module Components

**StatusService**
- `updateOrderStatus(orderId, newStatus)` - Main method for status updates
- `canTransition(currentStatus, newStatus)` - Validation logic for allowed transitions
- `cancelOrder(orderId)` - Convenience method for cancellations

**AutoCancellationTask**
- Scheduled task running every 5 minutes
- Finds orders with status READY where pickup time + 15 minutes has passed
- Automatically updates these orders to CANCELLED status

### 4.2 Staff Module Components

**StaffService**
- `getAllActiveOrders()` - Returns all non-archived orders
- `getOrdersByStatus(status)` - Filters orders by specific status
- `getOrderById(orderId)` - Retrieves single order with full details
- `archiveOrder(orderId)` - Marks order as archived
- `getArchivedOrders()` - Returns order history
- `getOrderStatistics()` - Returns map of status counts (e.g., PENDING: 5, ACCEPTED: 3)

**Required Repository Methods** (to be added to OrderRepository):
- `findByArchivedFalseOrderByPickupTimeAsc()` - Active orders sorted by time
- `findByStatusAndArchivedFalse(status)` - Filter by status, exclude archived
- `findByArchivedTrueOrderByUpdatedAtDesc()` - Archived orders, newest first

---

## 5. Database Requirements

### 5.1 Orders Table Additions

My modules require these columns in the orders table (coordinate with Parth and database team member F):

| Column Name | Type | Description |
|-------------|------|-------------|
| status | ENUM/VARCHAR | Current order status (PENDING, ACCEPTED, IN_PROGRESS, READY, COLLECTED, CANCELLED) |
| archived | BOOLEAN | Whether order is archived (default: false) |
| updated_at | TIMESTAMP | Last status update timestamp (for tracking) |
| created_at | TIMESTAMP | Order creation time (Parth's module) |

### 5.2 Indexes Needed

For performance optimization:
- Index on `status` column (frequent filtering)
- Index on `archived` column (frequent filtering)
- Index on `pickup_time` column (for auto-cancellation queries)
- Composite index on `(archived, status)` (common query pattern)

---

## 6. Status Transition Rules

### 6.1 Valid Transitions

| From Status | Can Transition To | Notes |
|-------------|-------------------|-------|
| PENDING | ACCEPTED, CANCELLED | Staff accepts or cancels new order |
| ACCEPTED | IN_PROGRESS, CANCELLED | Staff starts preparation or cancels |
| IN_PROGRESS | READY, CANCELLED | Order completed or cancelled |
| READY | COLLECTED, CANCELLED | Customer collects or auto-cancel after 15 min |
| COLLECTED | (none) | Terminal state |
| CANCELLED | (none) | Terminal state |

### 6.2 Invalid Transitions

- Cannot go backwards (e.g., READY → IN_PROGRESS)
- Cannot skip states (e.g., PENDING → READY)
- Cannot change COLLECTED or CANCELLED orders
- Any transition not listed in valid transitions table above

---

## 7. Auto-Cancellation Logic

### 7.1 Requirements

From project brief: "Cancel orders if the customer does not turn up 15 minutes after the provided time"

### 7.2 Implementation Approach

**Scheduled Task:**
- Runs every 5 minutes using Spring @Scheduled
- Query: Find orders where:
  - Status = READY
  - pickup_time + 15 minutes < current_time
  - archived = false
- For each order found: update status to CANCELLED

**Calculation:**
Cutoff time = current_time - 15 minutes
Find orders where: status = 'READY' AND pickup_time < cutoff_time

### 7.3 Edge Cases

- Orders already COLLECTED before 15 minutes: No action needed (status already terminal)
- Orders CANCELLED manually by staff: No action needed (already cancelled)
- Orders in IN_PROGRESS when 15 min passes: Not auto-cancelled (staff still working on it)

---

## 8. API Endpoints Overview

These endpoints will be implemented by the controller team member, but my services provide the backend logic:

### 8.1 Status Module Endpoints

| Method | Endpoint | Description | Service Method |
|--------|----------|-------------|----------------|
| PATCH | `/api/orders/{id}/status` | Update order status | StatusService.updateOrderStatus() |
| POST | `/api/orders/{id}/cancel` | Cancel specific order | StatusService.cancelOrder() |

### 8.2 Staff Module Endpoints

| Method | Endpoint | Description | Service Method |
|--------|----------|-------------|----------------|
| GET | `/api/staff/orders` | Get all active orders | StaffService.getAllActiveOrders() |
| GET | `/api/staff/orders?status=PENDING` | Filter by status | StaffService.getOrdersByStatus() |
| GET | `/api/staff/orders/{id}` | Get order details | StaffService.getOrderById() |
| POST | `/api/staff/orders/{id}/archive` | Archive order | StaffService.archiveOrder() |
| GET | `/api/staff/orders/archived` | View order history | StaffService.getArchivedOrders() |
| GET | `/api/staff/dashboard/stats` | Get status counts | StaffService.getOrderStatistics() |

---

## 9. Team Coordination

### 9.1 Critical Coordination with Parth (Person C)

**Must agree on:**

1. **OrderStatus Enum Values** - Exact same spelling and cases:
   - PENDING
   - ACCEPTED
   - IN_PROGRESS (not IN-PROGRESS or INPROGRESS)
   - READY
   - COLLECTED
   - CANCELLED (not CANCELED)

2. **Order Entity Fields:**
   - status field type (Enum)
   - archived field (boolean, default false)
   - updatedAt field (LocalDateTime)

3. **Package Structure:**
   - Where to place shared OrderStatus enum (in common package)
   - Naming convention for exceptions (OrderNotFoundException, InvalidStatusTransitionException)

4. **Repository Methods:**
   - Which custom queries are needed
   - Method naming conventions

### 9.2 Coordination with Database Team (Person F)

**Must communicate:**

1. **Table Schema Requirements:**
   - status column type (ENUM vs VARCHAR)
   - archived column (BOOLEAN)
   - updated_at column (TIMESTAMP)

2. **Indexes Needed:**
   - Index on status
   - Index on archived
   - Composite index on (archived, status)
   - Index on pickup_time

3. **Initial Data:**
   - No seed data needed for my modules (uses Parth's orders)

### 9.3 Coordination with Framework Integration (Person E)

**Must communicate:**

1. **Scheduled Task Configuration:**
   - Spring @EnableScheduling annotation needed in main application
   - Cron expression configuration: `@Scheduled(fixedRate = 300000)` // 5 minutes

2. **Transaction Management:**
   - Ensure @Transactional is properly configured
   - Handle concurrent status updates

---

## 10. Testing Considerations

### 10.1 Status Module Testing

**Key test cases:**
- Valid transitions work correctly
- Invalid transitions throw exceptions
- Auto-cancellation finds and cancels overdue orders
- Concurrent updates handled safely
- Terminal states cannot be changed

### 10.2 Staff Module Testing

**Key test cases:**
- Filter by status returns correct orders
- Archived orders excluded from active queries
- Only COLLECTED/CANCELLED orders can be archived
- Statistics calculated correctly
- Order detail retrieval works

---

## 11. Known Limitations & Future Enhancements

### 11.1 Current Limitations

- No status history tracking (only current status stored)
- No notification system when status changes
- Auto-cancellation runs every 5 minutes (not real-time at 15-minute mark)
- Simple authentication for staff (no role-based access control)

### 11.2 Possible Future Enhancements

- **Status History Table** - Track all status changes with timestamps and staff member who made change
- **Notification System** - Alert customers when status changes (email/SMS/push)
- **Real-time Updates** - WebSocket integration for live dashboard updates
- **Staff Authentication** - Individual staff accounts with audit trail
- **Analytics** - Average preparation time, peak hours, cancellation rates

---

## 12. Summary

My understanding of the Status and Staff requirements is clear and implementation planning is progressing. The design follows Spring Boot best practices with clean service layer separation and proper validation logic.

**Key Challenges:**
1. **Coordination with Parth** - Must align on Order entity structure and OrderStatus enum
2. **Transaction Safety** - Ensuring concurrent status updates don't conflict
3. **Auto-cancellation Timing** - Balancing accuracy (frequent checks) vs performance (resource usage)

**Next Steps:**
1. Finalize OrderStatus enum placement with Parth
2. Coordinate with Person F on database schema additions
3. Implement full StatusService with Order entity integration
4. Implement StaffService with repository queries
5. Create AutoCancellationTask scheduled component
6. Write unit tests for validation logic

This draft will be updated as implementation progresses and team decisions are finalized.

---

## 13. Package Structure
uk/ac/ncl/csc8019backend/business/
├── common/
│   └── OrderStatus.java (enum - shared with Parth)
│
├── status/
│   ├── StatusService.java
│   ├── AutoCancellationTask.java (future)
│   └── exceptions/ (future)
│       └── InvalidStatusTransitionException.java
│
└── staff/
├── StaffService.java
└── exceptions/ (future)
└── CannotArchiveException.java

---

**Document Version:** 1.0  
**Last Updated:** 21st April 2026  
**Status:** Initial implementation complete, awaiting Order entity integration
