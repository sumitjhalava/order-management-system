package com.hotwax.oms.model;

public class OrderItem {
    private long orderItemSeqId;
    private long orderId;
    private long productId;
    private int quantity;
    private String status;
    
    // Additional field for complete item details
    private Product product;

    public OrderItem() {}

    public long getOrderItemSeqId() { return orderItemSeqId; }
    public void setOrderItemSeqId(long orderItemSeqId) { this.orderItemSeqId = orderItemSeqId; }
    
    public long getOrderId() { return orderId; }
    public void setOrderId(long orderId) { this.orderId = orderId; }
    
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity; 
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}