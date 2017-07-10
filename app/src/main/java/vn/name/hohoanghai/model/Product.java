package vn.name.hohoanghai.model;

import java.io.Serializable;

/**
 * Created by hhhai0304 on 27/05/2016.
 */
public class Product implements Serializable {
    private String productID, productName, detail, avatar, avatarUrl, categoryID;
    private long price;

    public Product() {
    }

    public Product(String productID, String productName, String detail, String avatar, long price, String categoryID) {
        this.productID = productID;
        this.productName = productName;
        this.detail = detail;
        this.avatar = avatar;
        this.categoryID = categoryID;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}