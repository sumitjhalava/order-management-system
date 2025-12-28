package com.hotwax.oms.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrderHeader {
    private long orderId;
    private Date orderDate;
    private long customerId;
    private long shippingContactMechId;
    private long billingContactMechId;
    
    // Additional fields for complete order details
    private Customer customer;
    private ContactMech shippingAddress;
    private ContactMech billingAddress;
    private List<OrderItem> orderItems;

    public OrderHeader() {}

    public long getOrderId() { return orderId; }
    public void setOrderId(long orderId) { this.orderId = orderId; }
    
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    
    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    
    public long getShippingContactMechId() { return shippingContactMechId; }
    public void setShippingContactMechId(long shippingContactMechId) { this.shippingContactMechId = shippingContactMechId; }
    
    public long getBillingContactMechId() { return billingContactMechId; }
    public void setBillingContactMechId(long billingContactMechId) { this.billingContactMechId = billingContactMechId; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public ContactMech getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ContactMech shippingAddress) { this.shippingAddress = shippingAddress; }
    
    public ContactMech getBillingAddress() { return billingAddress; }
    public void setBillingAddress(ContactMech billingAddress) { this.billingAddress = billingAddress; }
    
    public List<OrderItem> getOrderItems() { 
        return orderItems == null ? null : Collections.unmodifiableList(orderItems); 
    }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}