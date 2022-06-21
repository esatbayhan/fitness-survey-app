package de.rub.selab22a15;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.rub.selab22a15.db.ForegroundApp;

public class MainActivity extends AppCompatActivity {

    private Button btnOpenActivityAccelerometerTest;
    private Button btnOpenActivitySurveyTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenActivityAccelerometerTest = findViewById(R.id.btnOpenActivityAccelerometerTest);
        btnOpenActivitySurveyTest = findViewById(R.id.btnOpenActivitySurveyTest);

        btnOpenActivityAccelerometerTest.setOnClickListener(new btnOpenActivityAccelerometerTestOnClick());
        btnOpenActivitySurveyTest.setOnClickListener(new btnOpenActivitySurveyTestOnClick());

        if (!foregroundServiceRunning()) {
            Intent serviceIntent = new Intent(this, ForegroundApp.class);
            startForegroundService(serviceIntent);
        }
    }

    public boolean foregroundServiceRunning() {

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (ForegroundApp.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private class btnOpenActivityAccelerometerTestOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AccelerometerTestActivity.class);
            startActivity(intent);
        }
    }

    private class btnOpenActivitySurveyTestOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SurveyTestActivity.class);
            startActivity(intent);
        }
    }
}