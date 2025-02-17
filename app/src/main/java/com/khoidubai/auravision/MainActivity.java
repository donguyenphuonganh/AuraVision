package com.khoidubai.auravision;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnCameraDetect = findViewById(R.id.btnCameraDetect);
        ImageButton btnLearning = findViewById(R.id.btnLearning);
        ImageButton btnBlindTest = findViewById(R.id.btnBlindTest);
        ImageButton btnChatBot = findViewById(R.id.btnChatBot);

        // Xử lý sự kiện cho từng nút
        btnCameraDetect.setOnClickListener(view -> {
            // Chuyển sang Activity Camera Detect (tạo mới nếu cần)
            Toast.makeText(MainActivity.this, "Chức năng Camera Detect đang phát triển!", Toast.LENGTH_SHORT).show();
        });

        btnLearning.setOnClickListener(view -> {
            // Chuyển sang Activity Learning
            Toast.makeText(MainActivity.this, "Chức năng Learning đang phát triển!", Toast.LENGTH_SHORT).show();
        });

        btnBlindTest.setOnClickListener(view -> {
            // Chuyển sang Activity Color Blindness Test
            Intent intent = new Intent(MainActivity.this, ColorBlindTestActivity.class);
            startActivity(intent);
        });

        btnChatBot.setOnClickListener(view -> {
            // Chuyển sang Activity ChatBot
            Toast.makeText(MainActivity.this, "Chức năng ChatBot đang phát triển!", Toast.LENGTH_SHORT).show();
        });
    }
}
