package de.rub.selab22a15.services;

import static de.rub.selab22a15.App.CHANNEL_ID_ACTIVITY_RECORD;
import static de.rub.selab22a15.App.CHANNEL_ID_NR_ACTIVITY_RECORD;

import android.app.Notification;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;

import de.rub.selab22a15.MainActivity;
import de.rub.selab22a15.R;
import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.db.Activity;

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

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_ACTIVITY_RECORD)
                .setContentTitle(getString(R.string.channelActivityRecordTitle))
                .setContentText(getString(R.string.channelActivityRecordText))
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(CHANNEL_ID_NR_ACTIVITY_RECORD, notification);
        sensorManager.registerListener(accelerometerEventListener, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        activity = null;
        timeElapsedRealtimeStarted = null;
        sensorManager.unregisterListener(accelerometerEventListener);
        super.onDestroy();
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
