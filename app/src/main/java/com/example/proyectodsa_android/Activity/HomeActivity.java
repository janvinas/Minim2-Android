package com.example.proyectodsa_android.Activity;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.InventoryObject;
import com.example.proyectodsa_android.ItemAdapter;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
public class HomeActivity extends AppCompatActivity {
    private TextView tvUsername;
    private Button btnLogout;
    private RecyclerView rvItems;
    private ItemAdapter adapter;
    private ApiService apiService;
    private String token;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        setupRecyclerView();
        loadUserData();
    }

    private void initializeViews() {
        tvUsername = findViewById(R.id.tvUsername);
        btnLogout = findViewById(R.id.btnLogout);
        rvItems = findViewById(R.id.rvItems);

        username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");
        tvUsername.setText(username);

        apiService = RetrofitClient.getInstance().getApi();

        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void setupRecyclerView() {
        adapter = new ItemAdapter();
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadUserData() {
        apiService.getUserObjects(username, token).enqueue(new Callback<List<InventoryObject>>() {
            @Override
            public void onResponse(Call<List<InventoryObject>> call, Response<List<InventoryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setItems(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<InventoryObject>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error loading items: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogout() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit().clear().apply();
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}