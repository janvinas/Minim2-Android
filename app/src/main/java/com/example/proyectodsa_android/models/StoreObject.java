package com.example.proyectodsa_android.models;

public class StoreObject {
    private String name;
    private double price;
    private String URL;

    // Default constructor required for Gson
    public StoreObject() {
    }

    public StoreObject(String name, double price, String URL) {
        this.name = name;
        this.price = price;
        this.URL = URL;
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

    public String getImageUrl() {
        return URL; // Getter for imageUrl
    }

    public void setImageUrl(String imageUrl) {
        this.URL = imageUrl; // Setter for imageUrl
    }

    @Override
    public String toString() {
        return "StoreObject{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + URL + '\'' + // Include imageUrl in toString
                '}';
    }
}