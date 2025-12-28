package com.hotwax.oms.model;

public class ContactMech {
    private long contactMechId;
    private long customerId;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;
    private String email;

    public ContactMech() {}

    public long getContactMechId() { return contactMechId; }
    public void setContactMechId(long contactMechId) { this.contactMechId = contactMechId; }
    
    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}