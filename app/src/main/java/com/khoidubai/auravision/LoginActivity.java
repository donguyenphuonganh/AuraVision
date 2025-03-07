package com.khoidubai.auravision;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLoginUserName, edtLoginPass;
    private Button btnLogin;
    private TextView tvReg;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginUserName = findViewById(R.id.edtLoginUserName);
        edtLoginPass = findViewById(R.id.edtLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvReg = findViewById(R.id.tvReg);

        sharedPreferences = getSharedPreferences("data_login", Context.MODE_PRIVATE);

        // Nhận dữ liệu từ RegisterActivity
        Intent intent = getIntent();
        if (intent != null) {
            String receivedUsername = intent.getStringExtra("username");
            String receivedPassword = intent.getStringExtra("password");

            if (receivedUsername != null) edtLoginUserName.setText(receivedUsername);
            if (receivedPassword != null) edtLoginPass.setText(receivedPassword);
        }

        // Sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Sự kiện đăng ký
        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String username = edtLoginUserName.getText().toString().trim();
        String password = edtLoginPass.getText().toString().trim();

        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (username.isEmpty()) {
            edtLoginUserName.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (password.isEmpty()) {
            edtLoginPass.setError("Vui lòng nhập mật khẩu");
            return;
        }

        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            edtLoginPass.setError("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }
}
