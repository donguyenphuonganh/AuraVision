package com.khoidubai.auravision;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerUsername, registerEmail, registerPassword;
    private Button btnReg;
    private CheckBox chkRememberRegister;
    private TextView txtErrRegisterUsername, txtErrRegisterEmail, txtErrRegisterPassword, tvBackToLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUsername = findViewById(R.id.registerUsername);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        btnReg = findViewById(R.id.btnReg);
        chkRememberRegister = findViewById(R.id.chkRememberRegister);
        txtErrRegisterUsername = findViewById(R.id.txtErrRegisterUsername);
        txtErrRegisterEmail = findViewById(R.id.txtErrRegisterEmail);
        txtErrRegisterPassword = findViewById(R.id.txtErrRegisterPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        sharedPreferences = getSharedPreferences("data_login", Context.MODE_PRIVATE);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String username = registerUsername.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        boolean hasError = false;

        if (username.isEmpty()) {
            txtErrRegisterUsername.setText("Tên không được để trống");
            txtErrRegisterUsername.setVisibility(View.VISIBLE);
            hasError = true;
        } else {
            txtErrRegisterUsername.setVisibility(View.GONE);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtErrRegisterEmail.setText("Email không hợp lệ");
            txtErrRegisterEmail.setVisibility(View.VISIBLE);
            hasError = true;
        } else {
            txtErrRegisterEmail.setVisibility(View.GONE);
        }

        if (password.isEmpty() || password.length() < 8) {
            txtErrRegisterPassword.setText("Mật khẩu phải có ít nhất 8 ký tự");
            txtErrRegisterPassword.setVisibility(View.VISIBLE);
            hasError = true;
        } else {
            txtErrRegisterPassword.setVisibility(View.GONE);
        }

        if (!hasError) {
            if (chkRememberRegister.isChecked()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.apply();
                Toast.makeText(this, "Đăng ký thành công và đã lưu thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        }
    }
}
