package com.example.proyectodsa_android;

public class StoreObject {
    private String name;
    private double price;

    // Default constructor required for Gson
    public StoreObject() {
    }

    public StoreObject(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StoreObject{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}