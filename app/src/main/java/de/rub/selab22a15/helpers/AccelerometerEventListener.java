package de.rub.selab22a15.helpers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import de.rub.selab22a15.database.research.Accelerometer;

public class AccelerometerEventListener implements SensorEventListener {
    private static final float EPSILON = 0.1f;
    private float x, y, z;
    private final AccelerometerWriter accelerometerWriter;
    private Long activityTimestamp;

    public AccelerometerEventListener(AccelerometerWriter accelerometerWriter) {
        super();

        this.accelerometerWriter = accelerometerWriter;
    }

    public AccelerometerEventListener(AccelerometerWriter accelerometerWriter, Long activityTimestamp) {
        this(accelerometerWriter);
        this.activityTimestamp = activityTimestamp;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.REPORTING_MODE_ON_CHANGE) {
            return;
        }

        long timestamp = System.currentTimeMillis();
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        if (Math.abs(this.x - x) < EPSILON &&
                Math.abs(this.y - y) < EPSILON &&
                Math.abs(this.z - z) < EPSILON) {
            return;
        }

        this.x = x;
        this.y = y;
        this.z = z;

        accelerometerWriter.write(new Accelerometer(
                timestamp,
                activityTimestamp,
                x,
                y,
                z
        ));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
