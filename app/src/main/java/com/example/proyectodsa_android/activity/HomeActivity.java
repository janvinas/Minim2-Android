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
    private Button btnUserStuff;
    private Button btnStore;
    private Button btnLogout;
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvUsername = findViewById(R.id.tvUsername);
        btnUserStuff = findViewById(R.id.btnUserStuff);
        btnStore = findViewById(R.id.btnstore);
        btnLogout = findViewById(R.id.btnLogout);

        String username = getIntent().getStringExtra("username");
        tvUsername.setText(username);
        String token = getIntent().getStringExtra("token");

        // 验证 token
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Token is missing or invalid!", Toast.LENGTH_SHORT).show();
            Log.e("HomeActivity", "Token is null or empty. Redirecting to login.");

            // 如果 token 无效，则返回登录界面
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Log.d("HomeActivity", "Token received: " + token);

        // 按钮点击事件

        btnStore.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoreActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("token", token);  // 确保 token 正确传递
            startActivity(intent);
        });

        btnUserStuff.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserStuffActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("token", token);  // 确保 token 正确传递
            startActivity(intent);
        });



        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        });
    }
}