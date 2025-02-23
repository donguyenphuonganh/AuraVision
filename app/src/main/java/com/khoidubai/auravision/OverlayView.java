package com.khoidubai.auravision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView extends View {
    private float touchX = -1, touchY = -1;
    private Paint circlePaint;

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE); // Màu viền vòng tròn
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setAntiAlias(true);
    }

    public void setTouchPosition(float x, float y) {
        touchX = x;
        touchY = y;
        invalidate(); // Vẽ lại view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (touchX >= 0 && touchY >= 0) {
            canvas.drawCircle(touchX, touchY, 20, circlePaint); // Vẽ vòng tròn
        }
    }
}