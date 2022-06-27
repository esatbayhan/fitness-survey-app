package de.rub.selab22a15.services;

import static de.rub.selab22a15.App.CHANNEL_ID;

import android.app.IntentService;
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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import de.rub.selab22a15.ActivityFragment;
import de.rub.selab22a15.MainActivity;
import de.rub.selab22a15.R;
import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;

public class ActivityRecordService extends Service {


    private AccelerometerRepository accelerometerRepository;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private SensorEventListener accelerometerEventListener;

    private float accelerometerX, accelerometerY, accelerometerZ;
    private final float EPSILON = 0.1f;

    private long activity_timestamp;

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
        activity_timestamp = intent.getLongExtra("activity_timestamp", 0);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Activity Record Service")
                .setContentText("Hello World")
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        sensorManager.registerListener(accelerometerEventListener, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                    new Accelerometer(timestamp, activity_timestamp, x, y, z));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
