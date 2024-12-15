package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.models.InventoryObject;
import com.example.proyectodsa_android.ItemAdapter;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.StoreAdapter;
import com.example.proyectodsa_android.models.StoreObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserStuffActivity extends AppCompatActivity {
    private TextView tvUsername, tvMoney;
    private RecyclerView rvInventory;
    private ItemAdapter inventoryAdapter;
    private ApiService apiService;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stuff);

        tvUsername = findViewById(R.id.tvUsername);
        tvMoney = findViewById(R.id.tvMoney);
        rvInventory = findViewById(R.id.rvInventory);
        Button btnBack = findViewById(R.id.btnBack);

        String username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");

        // 打印 token 到 Logcat
        Log.d("UserStuffActivity", "Received token: " + token);

        tvUsername.setText(username);

        inventoryAdapter = new ItemAdapter();
        rvInventory.setAdapter(inventoryAdapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getInstance().getApi();
        loadData(username);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadData(String username) {
        apiService.getUserObjects(username, token).enqueue(new Callback<List<InventoryObject>>() {
            @Override
            public void onResponse(Call<List<InventoryObject>> call, Response<List<InventoryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inventoryAdapter.setItems(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<InventoryObject>> call, Throwable t) {
                Toast.makeText(UserStuffActivity.this, "Error loading inventory", Toast.LENGTH_SHORT).show();
            }
        });

        // Load user money
        apiService.getUserMoney(username, token).enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvMoney.setText(String.format("%.2f €", response.body()));
                }
            }


            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Toast.makeText(UserStuffActivity.this, "Error loading money", Toast.LENGTH_SHORT).show();
            }
        });
    }
}