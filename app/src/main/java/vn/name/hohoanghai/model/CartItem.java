package vn.name.hohoanghai.model;

import java.io.Serializable;

/**
 * Created by hhhai0304 on 27/05/2016.
 */
public class CartItem implements Serializable{
    private String productID, productName, avatar;
    private int quantity;
    private long price;

    public CartItem() {
    }

    public CartItem(String productID, String productName, String avatar, int quantity, long price) {
        this.productID = productID;
        this.productName = productName;
        this.avatar = avatar;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}