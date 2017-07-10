package vn.name.hohoanghai.model;

import java.io.Serializable;

/**
 * Created by hhhai0304 on 25-06-2016.
 */
public class Order implements Serializable {
    private String orderID, email, name, orderDate, address, phone, orderStatus;
    private long price;

    public Order() {
    }

    public Order(String orderID, String email, String name, String orderDate, String address, String phone, long price, String orderStatus) {
        this.orderID = orderID;
        this.email = email;
        this.name = name;
        this.orderDate = orderDate;
        this.address = address;
        this.phone = phone;
        this.price = price;
        this.orderStatus = orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}