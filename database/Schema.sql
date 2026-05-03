create database whistlestop;

CREATE TABLE menu_items (
id SERIAL PRIMARY KEY,
name VARCHAR(100),
regular_price DECIMAL,
large_price DECIMAL,
available BOOLEAN DEFAULT TRUE
);

CREATE TABLE orders (
id SERIAL PRIMARY KEY,
customer_name VARCHAR(100),
email VARCHAR(100),
phone VARCHAR(20),
pickup_time TIMESTAMP,
total_price DECIMAL,
status VARCHAR(50) DEFAULT 'pending',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
id SERIAL PRIMARY KEY,
order_id INT REFERENCES orders(id),
menu_item_id INT REFERENCES menu_items(id),
quantity INT,
size VARCHAR(10),
price DECIMAL
);

CREATE TABLE users (
id SERIAL PRIMARY KEY,
name VARCHAR(100),
email VARCHAR(100) UNIQUE,
password TEXT
);



INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Latte', 3.00, 4.00, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Cappuccino', 3.20, 4.20, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Americano', 2.50, 3.50, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Mocha', 3.50, 4.50, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Flat White', 3.30, 4.30, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Espresso', 2.00, 2.80, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Macchiato', 2.70, 3.60, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Caramel Latte', 3.80, 4.80, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Vanilla Latte', 3.80, 4.80, TRUE);

INSERT INTO menu_items (name, regular_price, large_price, available)
VALUES ('Hot Chocolate', 3.00, 4.00, TRUE);