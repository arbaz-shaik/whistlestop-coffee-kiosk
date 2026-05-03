-- MENU ITEMS
INSERT INTO menu_items (name, description, regular_price, large_price, is_popular)
VALUES
('Americano','Espresso with hot water',1.50,2.00,TRUE),
('Americano with Milk','Americano with milk',2.00,2.50,FALSE),
('Latte','Smooth espresso with milk',2.50,3.00,TRUE),
('Cappuccino','Foamed milk coffee',2.50,3.00,FALSE),
('Hot Chocolate','Chocolate drink',2.00,2.50,FALSE),
('Mocha','Coffee with chocolate',2.50,3.00,FALSE),
('Mineral Water','Bottled water',1.00,NULL,FALSE);

-- STAFF USERS
INSERT INTO staff_users (username, password, role)
VALUES
('admin','$2a$10$hashvalue','ADMIN'),
('staff1','$2a$10$hashvalue','STAFF');