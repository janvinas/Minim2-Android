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


public class StoreActivity extends AppCompatActivity {
    private TextView tvUsername, tvMoney;
    private RecyclerView rvStore;
    private StoreAdapter storeAdapter;
    private ApiService apiService;
    private String token;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);


        tvUsername = findViewById(R.id.tvUsername);
        tvMoney = findViewById(R.id.tvMoney);
        rvStore = findViewById(R.id.rvStore);
        Button btnBack = findViewById(R.id.btnBack);

        username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");

        // 打印 token 进行调试
        Log.d("StoreActivity", "Token received: " + token);


        tvUsername.setText(username);



        // 确保 apiService 正确初始化
        apiService = RetrofitClient.getInstance().getApi();
        if (apiService == null) {
            Log.e("StoreActivity", "ApiService is null");
        }

        loadData(username);

        storeAdapter = new StoreAdapter();
        rvStore.setAdapter(storeAdapter);
        rvStore.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getInstance().getApi();

        storeAdapter.setOnItemClickListener(this::showPurchaseDialog);

        btnBack.setOnClickListener(v -> finish());
    }


    private void loadData(String username) {

        // 确保 apiService 已初始化
        if (apiService == null) {
            Log.e("StoreActivity", "ApiService is null");
            return;
        }


        // Load store items
        apiService.getStoreItems().enqueue(new Callback<List<StoreObject>>() {
            @Override
            public void onResponse(Call<List<StoreObject>> call, Response<List<StoreObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    storeAdapter.setItems(response.body());

                    // 打印整个 JSON 消息
                    String jsonResponse = response.body().toString(); // Assuming the response can be converted to a String directly
                    Log.d("StoreActivity", "JSON Response: " + jsonResponse);

                } else {
                    Toast.makeText(StoreActivity.this, "Error loading store items: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StoreObject>> call, Throwable t) {
                Toast.makeText(StoreActivity.this, "Error loading store items: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StoreActivity.this, "Error loading money", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 添加对话框
    private void showPurchaseDialog(StoreObject item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Purchase Confirmation");
        builder.setMessage("Do you want to purchase " + item.getName() + " for " + item.getPrice() + "?");

        // 确认购买按钮
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            purchaseItem(item); // 调用购买逻辑
            dialog.dismiss();
        });

        // 取消按钮
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        // 显示对话框
        builder.create().show();
    }


    private void purchaseItem(StoreObject item) {
        String cookieString = token;
        apiService.buyObject(item.getName(), username, 1, cookieString).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StoreActivity.this, "Purchase successful!", Toast.LENGTH_SHORT).show();
                    loadData(username); // Reload data to update UI
                } else {
                    Toast.makeText(StoreActivity.this, "Purchase failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(StoreActivity.this, "Purchase error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}