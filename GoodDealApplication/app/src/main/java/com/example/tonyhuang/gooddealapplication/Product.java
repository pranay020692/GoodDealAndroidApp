package com.example.tonyhuang.gooddealapplication;

/**
 * Created by puneet on 11/2/15.
 */
public class Product {

    private int id;
    private String productId;
    private String productName;
    private String productPrice;
    private String productAvgRating;

    public void setId(int id){
        this.id = id;
    }

    public void setProductId(String productId){
        this.productId = productId;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public void setProductPrice(String productPrice){
        this.productPrice = productPrice;
    }

    public void setProductAvgRating(String productAvgRating){
        this.productAvgRating = productAvgRating;
    }

    public int getId(){
        return id;
    }

    public String getProductId(){
        return productId;
    }

    public String getProductName(){
        return productName;
    }

    public String getProductPrice(){
        return productPrice;
    }

    public String getProductAvgRating(){
        return productAvgRating;
    }
}
