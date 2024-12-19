package com.example.proyectodsa_android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.IssueNotification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private TextView textTitle, textMessage;
    private String userID, token;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        textTitle = findViewById(R.id.textTitle);
        textMessage = findViewById(R.id.textMessage);
        userID = getIntent().getStringExtra("userID");
        token = getIntent().getStringExtra("token");
        apiService = RetrofitClient.getInstance().getApi();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnSend).setOnClickListener(v -> sendReport());
    }

    private void sendReport(){
        String title = textTitle.getText().toString();
        String message = textMessage.getText().toString();

        if(title.isEmpty()){
            Toast.makeText(ReportActivity.this,
                    "Debe rellenar el título",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(message.isEmpty()){
            Toast.makeText(ReportActivity.this,
                    "Debe escribir un mensaje",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(userID == null || token == null || userID.isEmpty() || token.isEmpty()){
            Toast.makeText(ReportActivity.this,
                    "Error interno",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        IssueNotification issue = new IssueNotification(new Date().toString(), title, message, userID);

        new AlertDialog.Builder(this)
                .setTitle("Denunciar abuso")
                .setMessage("Está seguro que quiere enviar este mensaje?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sí", (dialog, whichButton) -> sendRequest(issue))
                .setNegativeButton("No", null).show();

    }

    private void sendRequest(IssueNotification issue){
        apiService.reportAbuse(token, issue).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int code = response.code();
                if(code == 403){
                    // user unauthenticated
                    Toast.makeText(ReportActivity.this,
                            "Sesión no iniciada",
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ReportActivity.this, AuthActivity.class);
                    ReportActivity.this.startActivity(i);
                }else if(code == 400 || code == 500) {
                    Toast.makeText(ReportActivity.this,
                            "Error interno",
                            Toast.LENGTH_SHORT).show();
                }else if(code == 200){
                    Toast.makeText(ReportActivity.this,
                            "Mensaje enviado correctamente",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(ReportActivity.this,
                        "Error enviando mensaje",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}