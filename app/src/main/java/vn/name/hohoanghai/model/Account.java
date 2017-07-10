package vn.name.hohoanghai.model;

import java.io.Serializable;

/**
 * Created by hhhai0304 on 23-06-2016.
 */
public class Account implements Serializable {
    private String userID, email, name, address, district, city, phone, avatar;

    public Account() {
    }

    public Account(String userID, String email, String name, String address, String district, String city, String phone, String avatar) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.address = address;
        this.district = district;
        this.city = city;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}