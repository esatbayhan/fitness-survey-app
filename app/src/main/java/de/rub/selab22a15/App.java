package de.rub.selab22a15;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import de.rub.selab22a15.workers.AccelerometerRecordWorker;
import de.rub.selab22a15.workers.PeriodicNotificationWorker;


public class App extends Application {
    public static final String CHANNEL_ID_ACTIVITY_RECORD = "SERVICE_CHANNEL_ACTIVITY_RECORD";
    public static final String CHANNEL_NAME_ACTIVITY_RECORD = CHANNEL_ID_ACTIVITY_RECORD;

    public static final String CHANNEL_ID_PERIODIC_NOTIFICATION = "PERIODIC_NOTIFICATION";
    public static final String CHANNEL_NAME_PERIODIC_NOTIFICATION = CHANNEL_ID_PERIODIC_NOTIFICATION;
    public static final int NOTIFICATION_ID_PERIODIC_NOTIFICATION = 2;

    private static final String TAG_PASSIVE_RECORDING = "PASSIVE_RECORDING";
    private static final String TAG_PERIODIC_RECORDING = "PERIODIC_RECORDING";

    private static Application INSTANCE;

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

        createNotificationChannelService();
        createNotificationChannelPeriodicNotification();
        createWorkManagerPassiveRecording();
        createWorkManagerPeriodicNotification();
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

    private void createNotificationChannelPeriodicNotification() {
        NotificationChannel periodicNotificationNotificationChannel = new NotificationChannel(
                CHANNEL_ID_PERIODIC_NOTIFICATION,
                CHANNEL_NAME_PERIODIC_NOTIFICATION,
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(periodicNotificationNotificationChannel);
    }

    private void createWorkManagerPassiveRecording() {
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

    private void createWorkManagerPeriodicNotification() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                PeriodicNotificationWorker.class,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .addTag(TAG_PERIODIC_RECORDING)
                .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork(
                        TAG_PERIODIC_RECORDING,
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest
                );
    }
}