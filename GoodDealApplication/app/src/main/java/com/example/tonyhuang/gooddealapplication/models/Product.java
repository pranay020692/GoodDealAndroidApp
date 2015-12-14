package com.example.tonyhuang.gooddealapplication.models;

/**
 * Created by puneet on 11/2/15.
 */
public class Product {

    private int id;
    private String productId;
    private String productName;
    private String productPrice;
    private String productAvgRating;
    private String productImageUrl;

    public Product(int id, String productId, String productName, String productAvgRating, String productPrice, String productImageUrl) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productAvgRating = productAvgRating;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductAvgRating(String productAvgRating) {
        this.productAvgRating = productAvgRating;
    }

    public int getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductAvgRating() {
        return productAvgRating;
    }

    public String getProductImageUrl() {

        return productImageUrl;
    }
}
