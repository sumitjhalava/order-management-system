-- Data Insertion Script
USE order_management_system;

-- Insert Customers
INSERT INTO Customer (first_name, last_name) VALUES 
('John', 'Doe'),
('Jane', 'Smith');

-- Insert Contact Mechanisms for John Doe
INSERT INTO Contact_Mech (customer_id, street_address, city, state, postal_code, phone_number, email) VALUES 
(1, '1600 Amphitheatre Parkway', 'Mountain View', 'CA', '94043', '555-0101', 'john.doe@email.com'),
(1, '1 Infinite Loop', 'Cupertino', 'CA', '95014', '555-0102', 'john.doe.alt@email.com');

-- Insert Contact Mechanisms for Jane Smith
INSERT INTO Contact_Mech (customer_id, street_address, city, state, postal_code, phone_number, email) VALUES 
(2, '350 Fifth Avenue', 'New York', 'NY', '10118', '555-0201', 'jane.smith@email.com');

-- Insert Products
INSERT INTO Product (product_name, color, size) VALUES 
('T-Shirt', 'Red', 'M'),
('Jeans', 'Blue', '32'),
('Sneakers', 'White', '9'),
('Jacket', 'Black', 'L'),
('Hat', 'Green', 'One Size');