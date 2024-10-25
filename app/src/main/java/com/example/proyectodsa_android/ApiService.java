package com.example.proyectodsa_android;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register")
    Call<ApiResponse> registerUser(@Body User user);


    @POST("login")
    Call<ApiResponse> loginUser(@Body User user);
}
