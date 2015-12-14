package com.example.tonyhuang.gooddealapplication.models;

/**
 * Created by puneet on 11/12/15.
 */
public class History {

    final private int id;
    final private String name;
    final private String price;

    public History(int id, String name, String price) {
        this.id = id;
        this.name = name;
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
}
