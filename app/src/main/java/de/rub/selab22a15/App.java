package de.rub.selab22a15;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import de.rub.selab22a15.workers.AccelerometerRecordWorker;


public class App extends Application {
    public static final String CHANNEL_ID_ACTIVITY_RECORD = "SERVICE_CHANNEL_ACTIVITY_RECORD";
    public static final String CHANNEL_NAME_ACTIVITY_RECORD = CHANNEL_ID_ACTIVITY_RECORD;

    public static final String TAG_PASSIVE_RECORDING = "PASSIVE_RECORDING";

    private static Application INSTANCE;
    private static boolean IS_RUNNING_FOREGROUND;

    private static final long LOW_DURATION_SECONDS = 75;
    private static final long MEDIUM_DURATION_SECONDS = 125;
    private static final long HIGH_DURATION_SECONDS = 175;

    public static Application getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        createWorkManager();
        createNotificationChannelService();
    }

    private void createNotificationChannelService() {
        NotificationChannel serviceNotificationChannel = new NotificationChannel(
                CHANNEL_ID_ACTIVITY_RECORD,
                CHANNEL_NAME_ACTIVITY_RECORD,
                NotificationManager.IMPORTANCE_LOW
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceNotificationChannel);
    }

    private void createWorkManager() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                AccelerometerRecordWorker.class,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .addTag(TAG_PASSIVE_RECORDING)
                .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork(
                        TAG_PASSIVE_RECORDING,
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest
                );
    }
}