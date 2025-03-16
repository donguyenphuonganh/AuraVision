package com.khoidubai.auravision;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ColordetectionActivity extends AppCompatActivity {
    private PreviewView previewView;
    private TextView colorInfo, colorName;
    private OverlayViewRT overlayViewRT;
    private ExecutorService cameraExecutor;
    private static final int CAMERA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colordetection);

        previewView = findViewById(R.id.previewView);
        colorInfo = findViewById(R.id.colorInfo);
        colorName = findViewById(R.id.colorName);
        overlayViewRT = findViewById(R.id.overlayViewRT);
        cameraExecutor = Executors.newSingleThreadExecutor();

        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Log.e("ColorDetection", "Quyền camera bị từ chối!");
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, image -> {
                    int detectedColor = analyzeColor(image);
                    runOnUiThread(() -> {
                        updateUIColor(detectedColor);
                        overlayViewRT.setDetectedColor(detectedColor);
                    });
                    image.close();
                });

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
            } catch (Exception e) {
                Log.e("ColorDetection", "Lỗi khi khởi động camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private int analyzeColor(ImageProxy image) {
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;

        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        if (planes.length == 0 || planes[0].getBuffer() == null) {
            return Color.BLACK;
        }

        ByteBuffer buffer = planes[0].getBuffer();
        buffer.rewind();

        int pixelIndex = (centerY * image.getWidth() + centerX) * 4;
        if (pixelIndex + 3 >= buffer.remaining()) return Color.BLACK;

        buffer.position(pixelIndex);
        int r = buffer.get() & 0xFF;
        int g = buffer.get() & 0xFF;
        int b = buffer.get() & 0xFF;

        return Color.rgb(r, g, b);
    }

    private void updateUIColor(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        String colorLabel = getColorName(color);

        colorInfo.setText("Mã màu: " + hexColor);
        colorName.setText("Tên màu: " + colorLabel);
        colorInfo.setBackgroundColor(color);
        colorName.setBackgroundColor(color);
    }

    private String getColorName(int color) {
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("Đỏ", Color.RED);
        colorMap.put("Xanh dương", Color.BLUE);
        colorMap.put("Xanh lá", Color.GREEN);
        colorMap.put("Vàng", Color.YELLOW);
        colorMap.put("Đen", Color.BLACK);
        colorMap.put("Trắng", Color.WHITE);
        colorMap.put("Xám", Color.GRAY);
        colorMap.put("Cam", Color.rgb(255, 165, 0));
        colorMap.put("Tím", Color.rgb(128, 0, 128));
        colorMap.put("Hồng", Color.rgb(255, 182, 193));
        colorMap.put("Nâu", Color.rgb(139, 69, 19));
        colorMap.put("Xanh lục nhạt", Color.rgb(144, 238, 144));
        colorMap.put("Xanh nước biển", Color.rgb(0, 191, 255));
        colorMap.put("Xám đậm", Color.rgb(169, 169, 169));
        colorMap.put("Xanh dương nhạt", Color.rgb(173, 216, 230));
        colorMap.put("Đỏ đậm", Color.rgb(178, 34, 34));
        colorMap.put("Vàng nhạt", Color.rgb(255, 255, 153));
        colorMap.put("Xanh lam", Color.rgb(70, 130, 180));
        colorMap.put("Hồng đậm", Color.rgb(255, 105, 180));
        colorMap.put("Xanh lá đậm", Color.rgb(0, 100, 0));
        colorMap.put("Xanh cổ vịt", Color.rgb(0, 128, 128));
        colorMap.put("Xanh lam nhạt", Color.rgb(176, 224, 230));
        colorMap.put("Đỏ cam", Color.rgb(255, 69, 0));
        colorMap.put("Hồng đào", Color.rgb(255, 218, 185));
        colorMap.put("Be", Color.rgb(245, 245, 220));
        colorMap.put("Mận", Color.rgb(221, 160, 221));
        colorMap.put("Ngọc bích", Color.rgb(46, 139, 87));
        colorMap.put("Vàng cam", Color.rgb(255, 140, 0));
        colorMap.put("Xanh rêu", Color.rgb(85, 107, 47));
        colorMap.put("Đỏ tím", Color.rgb(199, 21, 133));


        String closestColor = "Không xác định";
        double minDiff = Double.MAX_VALUE;

        int r1 = Color.red(color);
        int g1 = Color.green(color);
        int b1 = Color.blue(color);

        for (Map.Entry<String, Integer> entry : colorMap.entrySet()) {
            int r2 = Color.red(entry.getValue());
            int g2 = Color.green(entry.getValue());
            int b2 = Color.blue(entry.getValue());

            double diff = Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));

            if (diff < minDiff) {
                minDiff = diff;
                closestColor = entry.getKey();
            }
        }

        return closestColor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}
