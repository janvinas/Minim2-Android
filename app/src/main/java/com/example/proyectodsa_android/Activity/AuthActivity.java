package com.example.proyectodsa_android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.LoginRequest;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private EditText etUsername, etEmail, etPassword;
    private EditText etLoginIdentifier, etLoginPassword;
    private Button btnRegister, btnLogin;
    private TextView tvSwitchToLogin, tvSwitchToRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initializeViews();
        apiService = RetrofitClient.getInstance().getApi();
        setupClickListeners();
    }

    private void initializeViews() {
        viewFlipper = findViewById(R.id.viewFlipper);

        // Register views
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvSwitchToLogin = findViewById(R.id.tvSwitchToLogin);

        // Login views
        etLoginIdentifier = findViewById(R.id.etLoginIdentifier);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSwitchToRegister = findViewById(R.id.tvSwitchToRegister);
    }

    private void setupClickListeners() {
        tvSwitchToLogin.setOnClickListener(v -> viewFlipper.setDisplayedChild(1));
        tvSwitchToRegister.setOnClickListener(v -> viewFlipper.setDisplayedChild(0));
        btnRegister.setOnClickListener(v -> handleRegister());
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleRegister() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setMail(email);

        apiService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    viewFlipper.setDisplayedChild(1);
                    etLoginIdentifier.setText(username);
                    etLoginPassword.setText(password);
                } else {
                    Toast.makeText(AuthActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogin() {
        String identifier = etLoginIdentifier.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest;
        if (Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            loginRequest = new LoginRequest(null, identifier, password);
        } else {
            loginRequest = new LoginRequest(identifier, null, password);
        }

        apiService.login(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    String token = response.headers().get("Set-Cookie");
                    saveLoginState(user.getUsername(), token);
                    startHomeActivity(user.getUsername(), token);
                } else {
                    Toast.makeText(AuthActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginState(String username, String token) {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        prefs.edit()
                .putString("username", username)
                .putString("token", token)
                .apply();
    }

    private void startHomeActivity(String username, String token) {
        Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("token", token);
        startActivity(intent);
        finish();
    }
}