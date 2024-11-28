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

public class HomeActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvMoney;
    private Button btnLogout;
    private Button btnSwitch;
    private ViewFlipper viewFlipper;
    private RecyclerView rvStore;
    private RecyclerView rvInventory;
    private StoreAdapter storeAdapter;
    private ItemAdapter inventoryAdapter;
    private ApiService apiService;
    private String token;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        setupRecyclerViews();
        loadData();
        storeAdapter.setOnItemClickListener(item -> showPurchaseDialog(item));
    }

    private void initializeViews() {
        tvUsername = findViewById(R.id.tvUsername);
        tvMoney = findViewById(R.id.tvMoney);
        btnLogout = findViewById(R.id.btnLogout);
        btnSwitch = findViewById(R.id.btnSwitch);
        viewFlipper = findViewById(R.id.viewFlipper);
        rvStore = findViewById(R.id.rvStore);
        rvInventory = findViewById(R.id.rvInventory);

        username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");
        tvUsername.setText(username);

        apiService = RetrofitClient.getInstance().getApi();

        btnLogout.setOnClickListener(v -> handleLogout());
        btnSwitch.setOnClickListener(v -> handleSwitch());
    }

    private void setupRecyclerViews() {
        // Setup Store RecyclerView
        storeAdapter = new StoreAdapter();
        rvStore.setAdapter(storeAdapter);
        rvStore.setLayoutManager(new LinearLayoutManager(this));

        // Setup Inventory RecyclerView
        inventoryAdapter = new ItemAdapter();
        rvInventory.setAdapter(inventoryAdapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        // Load store items
        apiService.getStoreItems().enqueue(new Callback<List<StoreObject>>() {
            @Override
            public void onResponse(Call<List<StoreObject>> call, Response<List<StoreObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    storeAdapter.setItems(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<StoreObject>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error loading store items", Toast.LENGTH_SHORT).show();
            }
        });

        // Load user items
        apiService.getUserObjects(username, token).enqueue(new Callback<List<InventoryObject>>() {
            @Override
            public void onResponse(Call<List<InventoryObject>> call, Response<List<InventoryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inventoryAdapter.setItems(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<InventoryObject>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error loading inventory", Toast.LENGTH_SHORT).show();
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
                Log.e("HomeActivity", "Error loading money: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Error loading money", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSwitch() {
        if (viewFlipper.getDisplayedChild() == 0) {
            viewFlipper.setDisplayedChild(1);
            btnSwitch.setText("View Store");
        } else {
            viewFlipper.setDisplayedChild(0);
            btnSwitch.setText("View Inventory");
        }
    }

    private void handleLogout() {
        // Borrar estado de inicio de sesión
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit().clear().apply();

        // Volver a la página de inicio de sesión
        Intent intent = new Intent(this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void handleTokenExpired() {
        Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent intent = new Intent(this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // 添加对话框
    private void showPurchaseDialog(StoreObject item) {
        new AlertDialog.Builder(this)
                .setTitle("Purchase Confirmation")
                .setMessage("Are you sure you want to buy " + item.getName() + "? Price: " + item.getPrice() + " €")
                .setPositiveButton("Confirm", (dialog, which) -> purchaseItem(item))
                .setNegativeButton("Cancel", null)
                .show();
    }

    //购买
    private void purchaseItem(StoreObject item) {
        apiService.buyObject(item.getName(), username, 1, token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Purchase successful!", Toast.LENGTH_SHORT).show();
                    loadData(); // 重新加载数据更新余额和物品清单
                } else {
                    Toast.makeText(HomeActivity.this, "Purchase failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Purchase error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}