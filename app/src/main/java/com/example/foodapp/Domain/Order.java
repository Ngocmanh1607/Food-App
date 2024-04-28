package com.example.foodapp.Domain;

import java.util.Date;
import java.util.List;

public class Order {
    private String userName;
    private String phone;
    private String location;
    private List<OrderItem> lOrderItem;
    private boolean status;
    private String dateTime;
    private double totalPrice;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order(String userName, String phone, String location, List<OrderItem> lOrderItem, boolean status, String dateTime, double total) {
        this.userName = userName;
        this.phone = phone;
        this.location = location;
        this.lOrderItem = lOrderItem;
        this.status = status;
        this.dateTime = dateTime;
        this.totalPrice=total;
    }

    public Order() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<OrderItem> getlOrderItem() {
        return lOrderItem;
    }

    public void setlOrderItem(List<OrderItem> lOrderItem) {
        this.lOrderItem = lOrderItem;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
