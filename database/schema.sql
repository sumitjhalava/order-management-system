-- Order Management System Database Schema

CREATE DATABASE IF NOT EXISTS order_management_system;
USE order_management_system;

-- 1. Customer Table
CREATE TABLE Customer (
    customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);

-- 2. Contact_Mech Table
CREATE TABLE Contact_Mech (
    contact_mech_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    street_address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

-- 3. Product Table
CREATE TABLE Product (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    color VARCHAR(30) NULL,
    size VARCHAR(10) NULL
);

-- 4. Order_Header Table
CREATE TABLE Order_Header (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    shipping_contact_mech_id BIGINT NOT NULL,
    billing_contact_mech_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (shipping_contact_mech_id) REFERENCES Contact_Mech(contact_mech_id),
    FOREIGN KEY (billing_contact_mech_id) REFERENCES Contact_Mech(contact_mech_id)
);

-- 5. Order_Item Table
CREATE TABLE Order_Item (
    order_item_seq_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Order_Header(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);