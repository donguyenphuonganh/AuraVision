package com.khoidubai.auravision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OverlayViewRT extends View {
    private Paint circlePaint;
    private int detectedColor = Color.WHITE;
    private int centerX, centerY;
    private int radius = 20;

    public OverlayViewRT(Context context) {
        super(context);
        init();
    }

    public OverlayViewRT(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
    }

    public void setDetectedColor(int color) {
        this.detectedColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Vẽ vòng tròn nhỏ ở tâm màn hình
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Vẽ ô màu hiển thị bên cạnh tâm
        Paint colorPaint = new Paint();
        colorPaint.setStyle(Paint.Style.FILL);
        colorPaint.setColor(detectedColor);
        canvas.drawRect(centerX + 50, centerY - 20, centerX + 90, centerY + 20, colorPaint);
    }
}
