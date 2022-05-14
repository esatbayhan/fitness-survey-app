package de.rub.selab22a15;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;

public class AccelerometerTestActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    private TextView tvAccelerometer;
    private Switch swAccelerometerActivated;
    private Button btnAccelerometerClear;

    private AccelerometerRepository accelerometerRepository;

    private SensorEventListener accelerometerEventListener;
    private float accelerometerX, accelerometerY, accelerometerZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_test);

        tvAccelerometer = findViewById(R.id.tvAccelerometer);
        swAccelerometerActivated = findViewById(R.id.swAccelerometerActivated);
        btnAccelerometerClear = findViewById(R.id.btnAccelerometerClear);
        accelerometerRepository = new AccelerometerRepository(getApplication());

        swAccelerometerActivated.setOnCheckedChangeListener(new swAccelerometerActivateEventListener());
        btnAccelerometerClear.setOnClickListener(new btnAccelerometerClearEventListener());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerEventListener = new AccelerometerEventListener();
    }

    class AccelerometerEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() != Sensor.REPORTING_MODE_ON_CHANGE) {
                return;
            }

            long timestamp = System.currentTimeMillis();
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if (accelerometerX == x && accelerometerY == y && accelerometerZ == z) {
                return;
            }

            accelerometerX = x;
            accelerometerY = y;
            accelerometerZ = z;

            tvAccelerometer.setText(String.format("x: %f y %f: z: %f", x, y, z));
            accelerometerRepository.insert(new Accelerometer(timestamp, x, y, z));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class btnAccelerometerClearEventListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            accelerometerRepository.delete();
        }
    }

    class swAccelerometerActivateEventListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                sensorManager.registerListener(accelerometerEventListener, sensorAccelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
            else {
                sensorManager.unregisterListener(accelerometerEventListener);
            }
        }
    }
}