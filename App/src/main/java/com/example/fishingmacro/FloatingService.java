package com.example.fishingmacro;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class FloatingService extends Service {
    private WindowManager windowManager;
    private TextView bubble;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        bubble = new TextView(this);
        bubble.setText("🎣");
        bubble.setTextSize(28);
        bubble.setGravity(Gravity.CENTER);
        bubble.setPadding(16, 12, 16, 12);
        bubble.setBackgroundColor(0xDDFFFFFF);

        int type = Build.VERSION.SDK_INT >= 26
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 40;
        params.y = 220;

        bubble.setOnTouchListener(new android.view.View.OnTouchListener() {
            private int startX, startY;
            private float touchX, touchY;

            @Override
            public boolean onTouch(android.view.View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = params.x;
                    startY = params.y;
                    touchX = event.getRawX();
                    touchY = event.getRawY();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = startX + (int)(event.getRawX() - touchX);
                    params.y = startY + (int)(event.getRawY() - touchY);
                    windowManager.updateViewLayout(bubble, params);
                    return true;
                }

                return true;
            }
        });

        windowManager.addView(bubble, params);
    }

    @Override
    public void onDestroy() {
        if (windowManager != null && bubble != null) {
            windowManager.removeView(bubble);
            bubble = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
