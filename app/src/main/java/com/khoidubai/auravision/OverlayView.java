package com.khoidubai.auravision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView extends View {
    private float touchX = -1;
    private float touchY = -1;
    private Paint circlePaint;
    private int circleColor = Color.WHITE;

    public OverlayView(Context context) {
        super(context);
        init();
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setAntiAlias(true);
    }

    public void setTouchPosition(float x, float y) {
        this.touchX = x;
        this.touchY = y;
        invalidate();
    }

    public void setCircleColor(int color) {
        this.circleColor = color;
        circlePaint.setColor(color);
        invalidate();
    }

    public void clearTouchPosition() {
        this.touchX = -1;
        this.touchY = -1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (touchX >= 0 && touchY >= 0) {
            canvas.drawCircle(touchX, touchY, 20, circlePaint);
        }
    }
}