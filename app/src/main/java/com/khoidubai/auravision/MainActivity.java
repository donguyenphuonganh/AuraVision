package com.khoidubai.auravision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import org.opencv.android.OpenCVLoader;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra xem OpenCV có load thành công không
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "✔ OpenCV Loaded Successfully!");
        } else {
            Log.e(TAG, "❌ OpenCV Load Failed!");
        }

        ImageButton btnCameraDetect = findViewById(R.id.btnCameraDetect);
        ImageButton btnLearning = findViewById(R.id.btnLearning);
        ImageButton btnBlindTest = findViewById(R.id.btnBlindTest);
        ImageButton btnChatBot = findViewById(R.id.btnChatBot);

        btnCameraDetect.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ColorpickerActivity.class);
            startActivity(intent);

        });

        btnLearning.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Chức năng Learning đang phát triển!", Toast.LENGTH_SHORT).show();
        });

        btnBlindTest.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ColorBlindTestActivity.class);
            startActivity(intent);
        });

        btnChatBot.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }
}
