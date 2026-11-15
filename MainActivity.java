package com.example.fishingmacro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int OVERLAY_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 48, 32, 32);

        TextView title = new TextView(this);
        title.setText("낚시 매크로\n\n화면 위에 작은 아이콘을 띄우는 테스트 앱입니다.\n현재 버전은 자동 낚시 동작을 수행하지 않습니다.");
        title.setTextSize(18);
        layout.addView(title);

        Button permission = new Button(this);
        permission.setText("다른 앱 위에 표시 허용");
        permission.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_REQUEST);
            } else {
                startOverlay();
            }
        });
        layout.addView(permission);

        Button start = new Button(this);
        start.setText("아이콘 띄우기");
        start.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
                startOverlay();
            } else {
                permission.performClick();
            }
        });
        layout.addView(start);

        setContentView(layout);
    }

    private void startOverlay() {
        Intent intent = new Intent(this, FloatingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}
