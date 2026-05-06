-- MENU
CREATE INDEX idx_menu_available ON menu_items(available);

-- ORDERS
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_pickup_time ON orders(pickup_time);
CREATE INDEX idx_orders_archived ON orders(archived);

-- ORDER ITEMS
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_menu ON order_items(menu_item_id);

-- STAFF
CREATE INDEX idx_staff_username ON staff_users(username);

-- PAYMENTS
CREATE INDEX idx_payments_order ON payments(order_id);