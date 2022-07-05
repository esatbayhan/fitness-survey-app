package de.rub.selab22a15.helpers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import de.rub.selab22a15.App;
import de.rub.selab22a15.database.research.Accelerometer;
import de.rub.selab22a15.database.research.AccelerometerRepository;

public class AccelerometerEventListener implements SensorEventListener {
    private static final String LOG_TAG = "ACCELEROMETER_EVENT_LISTENER";

    private final long bootTimeInMS;

    private static final long MINIMUM_DELAY_NS = 200_000_000; // 200MS
    private static final float MINIMUM_DIFFERENCE = 0.1f;
    private static long LAST_EVENT_TIMESTAMP_NS = 0; // elapsedRealtimeNanos

    private final AccelerometerRepository accelerometerRepository;
    private Long activityTimestamp;

    public AccelerometerEventListener() {
        Log.d(LOG_TAG, "Inside AccelerometerEventListener()");

        accelerometerRepository = new AccelerometerRepository(App.getInstance());
        bootTimeInMS = System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    public AccelerometerEventListener(Long activityTimestamp) {
        this();
        this.activityTimestamp = activityTimestamp;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(LOG_TAG, "Inside onSensorChanged(SensorEvent)");

        if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.d(LOG_TAG, "sensor is not of type linear acceleration");
            return;
        }

        if (event.timestamp - LAST_EVENT_TIMESTAMP_NS < MINIMUM_DELAY_NS) {
            return;
        }

        LAST_EVENT_TIMESTAMP_NS = event.timestamp;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if (Math.abs(x) < MINIMUM_DIFFERENCE &&
                Math.abs(y) < MINIMUM_DIFFERENCE &&
                Math.abs(z) < MINIMUM_DIFFERENCE) {
            return;
        }

        accelerometerRepository.insert(new Accelerometer(
                bootTimeInMS + TimeUnit.MILLISECONDS.convert(event.timestamp, TimeUnit.NANOSECONDS),
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
