package com.kinitoapps.myapplication;

public class FoodItem {


    String name;
    int id,avail,price;

    public FoodItem(int id,String name,int price, int avail) {
        this.id = id;
        this.name=name;
        this.price = price;
        this.avail = avail;
    }

    public String getName() {
        return name;
    }

    public int getAvail() {
        return avail;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setAvail(int avail) {
        this.avail = avail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
