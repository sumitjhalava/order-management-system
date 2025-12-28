package com.hotwax.oms.dao;

import com.hotwax.oms.model.*;
import com.hotwax.oms.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {

    public long createOrder(OrderHeader order) throws SQLException {
        String sql = "INSERT INTO Order_Header (order_date, customer_id, shipping_contact_mech_id, billing_contact_mech_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (order.getOrderDate() == null) {
                throw new IllegalArgumentException("Order date cannot be null");
            }
            stmt.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setLong(2, order.getCustomerId());
            stmt.setLong(3, order.getShippingContactMechId());
            stmt.setLong(4, order.getBillingContactMechId());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to create order");
    }

    public long createOrderItem(OrderItem item) throws SQLException {
        String sql = "INSERT INTO Order_Item (order_id, product_id, quantity, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, item.getOrderId());
            stmt.setLong(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setString(4, item.getStatus());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to create order item");
    }

    public OrderHeader getOrderById(long orderId) throws SQLException {
        String sql = "SELECT oh.*, c.first_name, c.last_name, " +
                   "sc.contact_mech_id as ship_id, sc.street_address as ship_street, sc.city as ship_city, " +
                   "sc.state as ship_state, sc.postal_code as ship_postal, sc.phone_number as ship_phone, sc.email as ship_email, " +
                   "bc.contact_mech_id as bill_id, bc.street_address as bill_street, bc.city as bill_city, " +
                   "bc.state as bill_state, bc.postal_code as bill_postal, bc.phone_number as bill_phone, bc.email as bill_email " +
                   "FROM Order_Header oh " +
                   "JOIN Customer c ON oh.customer_id = c.customer_id " +
                   "JOIN Contact_Mech sc ON oh.shipping_contact_mech_id = sc.contact_mech_id " +
                   "JOIN Contact_Mech bc ON oh.billing_contact_mech_id = bc.contact_mech_id " +
                   "WHERE oh.order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrderHeader order = new OrderHeader();
                    order.setOrderId(rs.getLong("order_id"));
                    order.setOrderDate(rs.getDate("order_date"));
                    order.setCustomerId(rs.getLong("customer_id"));
                    order.setShippingContactMechId(rs.getLong("shipping_contact_mech_id"));
                    order.setBillingContactMechId(rs.getLong("billing_contact_mech_id"));
                    
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getLong("customer_id"));
                    customer.setFirstName(rs.getString("first_name"));
                    customer.setLastName(rs.getString("last_name"));
                    order.setCustomer(customer);
                    
                    ContactMech shipping = new ContactMech();
                    shipping.setContactMechId(rs.getLong("ship_id"));
                    shipping.setStreetAddress(rs.getString("ship_street"));
                    shipping.setCity(rs.getString("ship_city"));
                    shipping.setState(rs.getString("ship_state"));
                    shipping.setPostalCode(rs.getString("ship_postal"));
                    shipping.setPhoneNumber(rs.getString("ship_phone"));
                    shipping.setEmail(rs.getString("ship_email"));
                    order.setShippingAddress(shipping);
                    
                    ContactMech billing = new ContactMech();
                    billing.setContactMechId(rs.getLong("bill_id"));
                    billing.setStreetAddress(rs.getString("bill_street"));
                    billing.setCity(rs.getString("bill_city"));
                    billing.setState(rs.getString("bill_state"));
                    billing.setPostalCode(rs.getString("bill_postal"));
                    billing.setPhoneNumber(rs.getString("bill_phone"));
                    billing.setEmail(rs.getString("bill_email"));
                    order.setBillingAddress(billing);
                    
                    order.setOrderItems(getOrderItems(orderId));
                    
                    return order;
                }
            }
        }
        return null;
    }

    public List<OrderItem> getOrderItems(long orderId) throws SQLException {
        String sql = "SELECT oi.*, p.product_name, p.color, p.size " +
                   "FROM Order_Item oi " +
                   "JOIN Product p ON oi.product_id = p.product_id " +
                   "WHERE oi.order_id = ?";
        
        List<OrderItem> items = new ArrayList<>();
        Map<Long, Product> productCache = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemSeqId(rs.getLong("order_item_seq_id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setStatus(rs.getString("status"));
                    
                    long productId = rs.getLong("product_id");
                    Product product = productCache.get(productId);
                    if (product == null) {
                        product = new Product();
                        product.setProductId(productId);
                        product.setProductName(rs.getString("product_name"));
                        product.setColor(rs.getString("color"));
                        product.setSize(rs.getString("size"));
                        productCache.put(productId, product);
                    }
                    item.setProduct(product);
                    
                    items.add(item);
                }
            }
        }
        return items;
    }

    public void updateOrder(long orderId, long shippingContactMechId, long billingContactMechId) throws SQLException {
        String sql = "UPDATE Order_Header SET shipping_contact_mech_id = ?, billing_contact_mech_id = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, shippingContactMechId);
            stmt.setLong(2, billingContactMechId);
            stmt.setLong(3, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Order not found with ID: " + orderId);
            }
        }
    }

    public void deleteOrder(long orderId) throws SQLException {
        String sql = "DELETE FROM Order_Header WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Order not found with ID: " + orderId);
            }
        }
    }

    public void updateOrderItem(int orderItemSeqId, int quantity, String status) throws SQLException {
        String sql = "UPDATE Order_Item SET quantity = ?, status = ? WHERE order_item_seq_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setString(2, status);
            stmt.setInt(3, orderItemSeqId);
            
            stmt.executeUpdate();
        }
    }

    public void deleteOrderItem(int orderItemSeqId) throws SQLException {
        String sql = "DELETE FROM Order_Item WHERE order_item_seq_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItemSeqId);
            stmt.executeUpdate();
        }
    }

    public List<OrderHeader> getAllOrders() throws SQLException {
        String sql = "SELECT oh.*, c.first_name, c.last_name " +
                   "FROM Order_Header oh " +
                   "JOIN Customer c ON oh.customer_id = c.customer_id " +
                   "ORDER BY oh.order_date DESC";
        
        List<OrderHeader> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderHeader order = new OrderHeader();
                    order.setOrderId(rs.getLong("order_id"));
                    order.setOrderDate(rs.getDate("order_date"));
                    order.setCustomerId(rs.getLong("customer_id"));
                    order.setShippingContactMechId(rs.getLong("shipping_contact_mech_id"));
                    order.setBillingContactMechId(rs.getLong("billing_contact_mech_id"));
                    
                    // Set basic customer info
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getLong("customer_id"));
                    customer.setFirstName(rs.getString("first_name"));
                    customer.setLastName(rs.getString("last_name"));
                    order.setCustomer(customer);
                    
                    // Get order items
                    order.setOrderItems(getOrderItems(rs.getLong("order_id")));
                    
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public boolean orderExists(long orderId) throws SQLException {
        String sql = "SELECT 1 FROM Order_Header WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean orderItemExists(int orderItemSeqId) throws SQLException {
        String sql = "SELECT 1 FROM Order_Item WHERE order_item_seq_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItemSeqId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}