package com.example.proyectodsa_android.models;

public class InventoryObject {
    private String name;
    private int quantity;
    private String URL;

    public InventoryObject(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return URL; // Getter for imageUrl
    }

    public void setImageUrl(String imageUrl) {
        this.URL = imageUrl; // Setter for imageUrl
    }

    @Override
    public String toString() {
        return "InventoryObject{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", imageUrl='" + URL + '\'' + // Include imageUrl in toString
                '}';
    }
}
