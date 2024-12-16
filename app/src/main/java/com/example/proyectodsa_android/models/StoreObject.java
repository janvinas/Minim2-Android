package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreObject {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("url")
    @Expose
    private String url;

    // Default constructor required for Gson
    public StoreObject() {
    }

    public StoreObject(String name, double price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return url; // Getter for imageUrl
    }

    public void setImageUrl(String imageUrl) {
        this.url = imageUrl; // Setter for imageUrl
    }

    @Override
    public String toString() {
        return "StoreObject{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + url + '\'' + // Include imageUrl in toString
                '}';
    }
}