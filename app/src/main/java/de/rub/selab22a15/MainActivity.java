package de.rub.selab22a15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnOpenActivityAccelerometerTest;
    private Button btnOpenActivitySurveyTest;
    private Button btnOpenActivityGPS;
    private Button btnOpenActivityExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenActivityAccelerometerTest = findViewById(R.id.btnOpenActivityAccelerometerTest);
        btnOpenActivitySurveyTest = findViewById(R.id.btnOpenActivitySurveyTest);
        btnOpenActivityGPS = findViewById(R.id.btnOpenActivityGPS);
        btnOpenActivityExport = findViewById(R.id.btnOpenActivityExport);

        btnOpenActivityAccelerometerTest.setOnClickListener(new btnOpenActivityAccelerometerTestOnClick());
        btnOpenActivitySurveyTest.setOnClickListener(new btnOpenActivitySurveyTestOnClick());
        btnOpenActivityGPS.setOnClickListener(new btnOpenActivityGPS());
        btnOpenActivityExport.setOnClickListener(new btnOpenActivityExport());
    }

    private class btnOpenActivityAccelerometerTestOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AccelerometerTestActivity.class);
            startActivity(intent);
        }
    }

    private class btnOpenActivityGPS implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent (MainActivity.this, GPSActivity.class);
            startActivity(intent);
        }
    }

    private class btnOpenActivityExport implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ExportDatabaseActivity.class);
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