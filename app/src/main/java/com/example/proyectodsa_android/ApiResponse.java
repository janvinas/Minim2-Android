package com.example.proyectodsa_android;

public class ApiResponse {
    private boolean success;
    private String message;
    private String token;  // JWT token or session token

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
}
