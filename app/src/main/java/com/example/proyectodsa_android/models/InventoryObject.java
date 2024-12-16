package com.example.proyectodsa_android.models;

public class InventoryObject {
    private String objectID;    // 物品ID
    private int quantity;       // 数量
    private String name;        // 显示用的名字
    private String URL;         // 图片URL

    // 构造函数
    public InventoryObject() {}

    // Getters and setters
    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return URL;
    }

    public void setImageUrl(String URL) {
        this.URL = URL;
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
