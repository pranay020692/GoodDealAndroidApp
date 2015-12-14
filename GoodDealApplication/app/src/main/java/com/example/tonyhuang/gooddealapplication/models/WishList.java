package com.example.tonyhuang.gooddealapplication.models;

/**
 * Created by puneet on 11/12/15.
 */
public class WishList {

    final private int id;
    final private String name;
    final private String imageUrl;
    final private String price;

    public WishList(int id, String name, String imageUrl, String price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {

        return price;
    }

    public String getImageUrl() {

        return imageUrl;
    }
}
