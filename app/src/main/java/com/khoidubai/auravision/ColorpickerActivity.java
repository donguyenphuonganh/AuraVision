package com.khoidubai.auravision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ColorpickerActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView colorInfo;
    private TextView titleText;
    private MaterialButton btnSelectImage;
    private Bitmap bitmap;
    private OverlayView overlayView;

    private List<ColorData> colorList = new ArrayList<>();
    private static final int PICK_IMAGE = 1;

    // Nhóm màu chính
    private static final String[][] COLOR_GROUPS = {
            {"Red", "255,0,0"},
            {"Green", "0,255,0"},
            {"Blue", "0,0,255"},
            {"Yellow", "255,255,0"},
            {"Cyan", "0,255,255"},
            {"Magenta", "255,0,255"},
            {"White", "255,255,255"},
            {"Black", "0,0,0"},
            {"Gray", "128,128,128"},
            {"Brown", "139,69,19"},
            {"Orange", "255,165,0"},
            {"Pink", "255,192,203"},
            {"Purple", "128,0,128"},
            // Các nhóm màu bổ sung
            {"Light Blue", "173,216,230"},
            {"Dark Blue", "0,0,139"},
            {"Lime", "0,255,0"},
            {"Teal", "0,128,128"},
            {"Maroon", "128,0,0"},
            {"Olive", "128,128,0"},
            {"Beige", "245,245,220"},
            {"Gold", "255,215,0"},
            {"Lavender", "230,230,250"},
            {"Turquoise", "64,224,208"},
            {"Salmon", "250,128,114"},
            {"Indigo", "75,0,130"},
            {"Ivory", "255,255,240"},
            {"Chocolate", "210,105,30"}


};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);

        Button btnRealTimeDetection = findViewById(R.id.btnRealTimeDetection);
        btnRealTimeDetection.setOnClickListener(v -> {
            Intent intent = new Intent(ColorpickerActivity.this, ColordetectionActivity.class);
            startActivity(intent);
        });

        // Khởi tạo views
        imageView = findViewById(R.id.imageView);
        colorInfo = findViewById(R.id.colorInfo);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        overlayView = findViewById(R.id.overlayView);
        titleText = findViewById(R.id.tv_title);

        // Hiệu ứng fade-in cho tiêu đề
        AlphaAnimation fadeInTitle = new AlphaAnimation(0.0f, 1.0f);
        fadeInTitle.setDuration(1000);
        titleText.startAnimation(fadeInTitle);
        titleText.setAlpha(1.0f);

        // Đọc dữ liệu từ colors.csv
        loadColorsFromCSV();

        // Chọn ảnh từ thư viện
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Xử lý khi chạm vào ảnh
        imageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                if (bitmap == null) {
                    colorInfo.setText("Lỗi: Vui lòng chọn ảnh trước!");
                    return true;
                }

                // Scale lại tọa độ cho bitmap
                int imageViewWidth = imageView.getWidth();
                int imageViewHeight = imageView.getHeight();
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();

                float scaleX = (float) bitmapWidth / imageViewWidth;
                float scaleY = (float) bitmapHeight / imageViewHeight;

                int scaledX = (int) (x * scaleX);
                int scaledY = (int) (y * scaleY);

                if (scaledX >= bitmapWidth || scaledY >= bitmapHeight || scaledX < 0 || scaledY < 0) {
                    colorInfo.setText("Lỗi: Tọa độ ngoài vùng ảnh!");
                    return true;
                }

                // Lấy màu trung bình xung quanh điểm chạm
                int radius = 2;
                int avgRed = 0, avgGreen = 0, avgBlue = 0, count = 0;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        int nx = scaledX + dx;
                        int ny = scaledY + dy;

                        if (nx >= 0 && ny >= 0 && nx < bitmapWidth && ny < bitmapHeight) {
                            int pixel = bitmap.getPixel(nx, ny);
                            avgRed += Color.red(pixel);
                            avgGreen += Color.green(pixel);
                            avgBlue += Color.blue(pixel);
                            count++;
                        }
                    }
                }

                if (count > 0) {
                    avgRed /= count;
                    avgGreen /= count;
                    avgBlue /= count;
                }

                // Tìm màu gần nhất
                String colorName = getNearestColorName(avgRed, avgGreen, avgBlue);
                String colorGroup = getNearestColorGroup(avgRed, avgGreen, avgBlue);

                // Cập nhật overlay để hiển thị vòng tròn
                overlayView.setTouchPosition(x, y);
                // Đổi màu vòng tròn overlay thành màu được nhận diện
                overlayView.setCircleColor(Color.rgb(avgRed, avgGreen, avgBlue));

                // Hiển thị thông tin màu
                colorInfo.setText(String.format("Màu: %s\nNhóm: %s\nRGB: %d, %d, %d", colorName, colorGroup, avgRed, avgGreen, avgBlue));
            }
            return true;
        });
    }

    // Đọc dữ liệu từ colors.csv
    private void loadColorsFromCSV() {
        try {
            InputStream is = getAssets().open("colors.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String name = parts[1].trim();
                    int r = Integer.parseInt(parts[3].trim());
                    int g = Integer.parseInt(parts[4].trim());
                    int b = Integer.parseInt(parts[5].trim());
                    colorList.add(new ColorData(name, r, g, b));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            colorInfo.setText("Lỗi: Không thể tải dữ liệu màu!");
        }
    }

    // Tìm màu gần nhất bằng khoảng cách Euclidean
    private String getNearestColorName(int r, int g, int b) {
        String closestColor = "Unknown";
        double minDistance = Double.MAX_VALUE;

        for (ColorData color : colorList) {
            double distance = Math.sqrt(Math.pow(color.r - r, 2) + Math.pow(color.g - g, 2) + Math.pow(color.b - b, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = color.name;
            }
        }
        return closestColor;
    }

    // Tìm nhóm màu chính gần nhất
    private String getNearestColorGroup(int r, int g, int b) {
        String closestGroup = "Unknown";
        double minDistance = Double.MAX_VALUE;

        for (String[] group : COLOR_GROUPS) {
            String name = group[0];
            String[] rgb = group[1].split(",");
            int groupR = Integer.parseInt(rgb[0]);
            int groupG = Integer.parseInt(rgb[1]);
            int groupB = Integer.parseInt(rgb[2]);

            double distance = Math.sqrt(Math.pow(groupR - r, 2) + Math.pow(groupG - g, 2) + Math.pow(groupB - b, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestGroup = name;
            }
        }
        return closestGroup;
    }

    // Xử lý khi chọn ảnh từ thư viện
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);

                // Hiệu ứng fade-in khi ảnh được tải
                AlphaAnimation fadeInImage = new AlphaAnimation(0.0f, 1.0f);
                fadeInImage.setDuration(1000);
                imageView.startAnimation(fadeInImage);
                imageView.setAlpha(1.0f);

                // Reset thông tin màu
                colorInfo.setText("Màu: ---\nNhóm: ---\nRGB: ---, ---, ---");
                overlayView.clearTouchPosition();
            } catch (IOException e) {
                e.printStackTrace();
                colorInfo.setText("Lỗi: Không thể tải ảnh!");
            }
        }
    }

    // Lớp lưu thông tin màu
    private static class ColorData {
        String name;
        int r, g, b;

        ColorData(String name, int r, int g, int b) {
            this.name = name;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}