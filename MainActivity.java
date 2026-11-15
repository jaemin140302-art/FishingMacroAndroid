
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 48, 32, 32);

        TextView title = new TextView(this);
        title.setText("낚시 매크로\\n낚시 아이콘 테스트 앱");
        title.setTextSize(22);
        layout.addView(title);

        Button permission = new Button(this);
        permission.setText("화면 위에 표시 허용");
        layout.addView(permission);

        permission.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 &&
                    !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())
                );
                startActivity(intent);
            } else {
                startFloating();
            }
        });

        Button start = new Button(this);
        start.setText("낚시 아이콘 실행");
        layout.addView(start);

        start.setOnClickListener(v -> startFloating());

        setContentView(layout);
    }

    private void startFloating() {
        if (Build.VERSION.SDK_INT < 23 ||
                Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(this, FloatingService.class);

            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
    }
}
