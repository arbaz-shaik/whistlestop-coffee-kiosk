# Database Setup Instructions

## Step 1: Create Database
CREATE DATABASE whistlestop_db;
USE whistlestop_db;

## Step 2: Run Schema
mysql -u root -p whistlestop_db < schema.sql

## Step 3: Insert Data
mysql -u root -p whistlestop_db < seed-data.sql

## Step 4: Add Indexes
mysql -u root -p whistlestop_db < indexes.sql

## Step 5: Verify
SHOW TABLES;
SELECT * FROM menu_items;

## Default Login
Username: admin
Password: admin123

## Reset Database
mysql -u root -p whistlestop_db < drop-tables.sql
