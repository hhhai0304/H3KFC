package vn.name.hohoanghai.model;

import java.io.Serializable;

/**
 * Created by hhhai0304 on 03/06/2016.
 */
public class Category implements Serializable {
    private String categoryID, name, detail;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}