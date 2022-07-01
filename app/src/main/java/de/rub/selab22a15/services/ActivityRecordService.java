package de.rub.selab22a15.services;

import static de.rub.selab22a15.helpers.ServiceNotification.NOTIFICATION_ID;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.db.Activity;
import de.rub.selab22a15.helpers.ServiceNotification;

public class ActivityRecordService extends Service {
    private static final String LOG_ACTIVITY_SERVICE = "ACTIVITY_SERVICE";
    private static Long timeElapsedRealtimeStarted;
    private static boolean isRunning;
    private static Activity activity;
    private static final float EPSILON = 0.1f;

    private float accelerometerX, accelerometerY, accelerometerZ;

    private AccelerometerRepository accelerometerRepository;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private SensorEventListener accelerometerEventListener;

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setActivity(Activity activity) {
        ActivityRecordService.activity = activity;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static long getTimeElapsedRealtimeStarted() {
        return timeElapsedRealtimeStarted;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        accelerometerRepository = new AccelerometerRepository(getApplication());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerEventListener = new AccelerometerEventListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (activity == null) {
            Log.w(LOG_ACTIVITY_SERVICE,
                    "ActivityRecordService.activity is null and has to be set before.");
            stopSelf();
        }

        isRunning = true;
        timeElapsedRealtimeStarted = SystemClock.elapsedRealtime();

        startForeground(NOTIFICATION_ID, ServiceNotification.getNotification(this));
        sensorManager.registerListener(accelerometerEventListener, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        activity = null;
        timeElapsedRealtimeStarted = null;
        sensorManager.unregisterListener(accelerometerEventListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class AccelerometerEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() != Sensor.REPORTING_MODE_ON_CHANGE) {
                return;
            }

            if (activity == null) {
                return;
            }

            long timestamp = System.currentTimeMillis();
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if (Math.abs(accelerometerX - x) < EPSILON &&
                    Math.abs(accelerometerY - y) < EPSILON &&
                    Math.abs(accelerometerZ - z) < EPSILON) {
                return;
            }

            accelerometerX = x;
            accelerometerY = y;
            accelerometerZ = z;

            accelerometerRepository.insert(
                    new Accelerometer(timestamp, activity.getTimestamp(), x, y, z));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
