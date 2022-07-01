package de.rub.selab22a15;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import de.rub.selab22a15.workers.AccelerometerRecordWorker;


public class App extends Application {
    public static final int CHANNEL_ID_NR_ACTIVITY_RECORD = 1;
    public static final String CHANNEL_ID_ACTIVITY_RECORD = "SERVICE_CHANNEL_ACTIVITY_RECORD";
    public static final String CHANNEL_NAME_ACTIVITY_RECORD = "SERVICE_CHANNEL_ACTIVITY_RECORD";

    private static final String TAG_PASSIVE_RECORDING = "PASSIVE_RECORDING";

    public static final String CHANNEL_ID_TEST = "CHANNEL_TEST";
    public static final String CHANNEL_NAME_TEST = "CHANNEL_TEST";

    private static Application INSTANCE;

    private static long MIN_DURATION_SECONDS = 75;
    private static long MEDIUM_DURATION_SECONDS = 125;
    private static long HIGH_DURATION_SECONDS = 175;

    public static Application getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannelTest();
        createNotificationChannelService();
        createWorkManager();

        INSTANCE = this;
    }

    private void createNotificationChannelTest() {
        NotificationChannel testChannel = new NotificationChannel(
                CHANNEL_ID_TEST,
                CHANNEL_NAME_TEST,
                NotificationManager.IMPORTANCE_HIGH
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(testChannel);
    }

    private void createNotificationChannelService() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID_ACTIVITY_RECORD,
                CHANNEL_NAME_ACTIVITY_RECORD,
                NotificationManager.IMPORTANCE_LOW
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    private void createWorkManager() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(AccelerometerRecordWorker.class,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS
        )
                .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork(
                        TAG_PASSIVE_RECORDING,
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest
                );
    }
}