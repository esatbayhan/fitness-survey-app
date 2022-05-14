package de.rub.selab22a15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnOpenActivityAccelerometerTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenActivityAccelerometerTest = findViewById(R.id.btnOpenActivityAccelerometerTest);
        btnOpenActivityAccelerometerTest.setOnClickListener(new btnOpenActivityAccelerometerTestOnClickListener());
    }

    private class btnOpenActivityAccelerometerTestOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AccelerometerTestActivity.class);
            startActivity(intent);
        }
    }
}