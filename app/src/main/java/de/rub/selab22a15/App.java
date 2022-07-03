package de.rub.selab22a15;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import de.rub.selab22a15.workers.AccelerometerRecordWorker;
import de.rub.selab22a15.workers.PeriodicNotificationWorker;


public class App extends Application {
    public static final String CHANNEL_ID_ACTIVITY_RECORD = "SERVICE_CHANNEL_ACTIVITY_RECORD";
    public static final String CHANNEL_NAME_ACTIVITY_RECORD = CHANNEL_ID_ACTIVITY_RECORD;

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

        AccelerometerRecordWorker.start(getApplicationContext());
//        PeriodicNotificationWorker.start(getApplicationContext());

        createServiceNotificationChannel();
    }

    private void createServiceNotificationChannel() {
        NotificationChannel serviceNotificationChannel = new NotificationChannel(
                CHANNEL_ID_ACTIVITY_RECORD,
                CHANNEL_NAME_ACTIVITY_RECORD,
                NotificationManager.IMPORTANCE_LOW
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceNotificationChannel);
    }
}