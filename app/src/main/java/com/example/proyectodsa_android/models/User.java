package com.example.proyectodsa_android.models;

public class User {
    private String username;
    private String password;
    private String mail;
    private double money;

    public User() {
        this.money = 50;
    }

    public User(String username, String password, String mail) {
        this();
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getMail() { return mail; }

    public void setMail(String mail) {
        this.mail = mail;
    }
}