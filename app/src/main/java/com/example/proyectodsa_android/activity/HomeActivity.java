package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit().clear().apply();
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}