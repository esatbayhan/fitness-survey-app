package de.rub.selab22a15.services;

import static de.rub.selab22a15.helpers.ServiceNotification.NOTIFICATION_ID;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import de.rub.selab22a15.database.research.Activity;
import de.rub.selab22a15.helpers.AccelerometerEventListener;
import de.rub.selab22a15.helpers.ServiceNotification;
import de.rub.selab22a15.receivers.LongRunReceiver;

public class AccelerometerRecordService extends Service {
    private static final String LOG_TAG = "ACCELEROMETER_RECORD_SERVICE";
    public static final String EXTRA_IS_ACTIVE_RECORDING = "IS_ACTIVE_RECORDING";

    private static final long LONG_RUN_DELAY_MS = 10800000; // 3 Hours

    private static Long timeElapsedRealtimeStarted;
    private static boolean isActiveRecording;
    private static Activity activity;

    private SensorManager sensorManager;
    private SensorEventListener accelerometerEventListener;

    public static boolean isActiveRecording() {
        return isActiveRecording;
    }

    public static void setActivity(Activity activity) {
        AccelerometerRecordService.activity = activity;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static long getTimeElapsedRealtimeStarted() {
        return timeElapsedRealtimeStarted;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Inside onStartCommand(Intent, int, int)");

        isActiveRecording = intent.getBooleanExtra(EXTRA_IS_ACTIVE_RECORDING, false);
        Long timestamp = null;

        if (isActiveRecording) {
            if (activity == null) {
                Log.w(LOG_TAG, "isActive recording and activity is null. It has to be set before.");
                stopSelf();
            }

            handleLongRun();
            timestamp = activity.getTimestamp();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerEventListener = new AccelerometerEventListener(timestamp);

        timeElapsedRealtimeStarted = SystemClock.elapsedRealtime();

        startForeground(NOTIFICATION_ID, ServiceNotification.getNotification(this));

        sensorManager.registerListener(
                accelerometerEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);

        handleLongRun();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isActiveRecording = false;
        activity = null;
        timeElapsedRealtimeStarted = null;
        sensorManager.unregisterListener(accelerometerEventListener);
        stopLongRun();
    }

    private void stopLongRun() {
        Log.d(LOG_TAG, "stopLongRun()");

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(
                Context.ALARM_SERVICE);
        alarmManager.cancel(getLongRunIntent());
    }

    private void handleLongRun() {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(
                Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getLongRunIntent();
        alarmManager.cancel(pendingIntent);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + LONG_RUN_DELAY_MS,
                pendingIntent);
    }

    private PendingIntent getLongRunIntent() {
        Log.d(LOG_TAG, "getLongRunIntent()");

        Intent intent = new Intent(getApplicationContext(), LongRunReceiver.class);

        return PendingIntent.getBroadcast(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
