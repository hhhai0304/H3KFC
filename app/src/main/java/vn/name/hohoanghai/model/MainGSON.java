package vn.name.hohoanghai.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hhhai0304 on 02/06/2016.
 */
public class MainGSON implements Serializable {
    ArrayList<Product> products;
    ArrayList<Category> categories;
    ArrayList<Order> orders;
    ArrayList<OrderDetail> orderDetails;

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}