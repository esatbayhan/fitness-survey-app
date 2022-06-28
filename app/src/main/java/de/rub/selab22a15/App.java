package de.rub.selab22a15;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;


public class App extends Application {
    public static final String CHANNEL_ID_ACTIVITY_RECORD = "activityTrackingServiceChannel";
    public static final int CHANNEL_ID_NR_ACTIVITY_RECORD = 1;
    public static final String CHANNEL_NAME_ACTIVITY_RECORD = "Activity Tracking Service Channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID_ACTIVITY_RECORD,
                CHANNEL_NAME_ACTIVITY_RECORD,
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }
}