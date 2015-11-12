package com.example.tonyhuang.gooddealapplication;

/**
 * Created by puneet on 11/12/15.
 */
public class WishList {

    final private int id;
    final private String name;

    public WishList(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
