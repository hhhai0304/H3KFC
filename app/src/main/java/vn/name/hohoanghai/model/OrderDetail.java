package vn.name.hohoanghai.model;

import java.io.Serializable;

/**
 * Created by hhhai0304 on 25-06-2016.
 */
public class OrderDetail implements Serializable {
    private String productName, detailID, orderID, productID, quantity;

    public OrderDetail() {
    }

    public OrderDetail(String productName, String detailID, String orderID, String productID, String quantity) {
        this.productName = productName;
        this.detailID = detailID;
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDetailID() {
        return detailID;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}