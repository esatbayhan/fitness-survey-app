package de.rub.selab22a15.helpers;

import static de.rub.selab22a15.App.CHANNEL_ID_ACTIVITY_RECORD;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import de.rub.selab22a15.R;

public class ServiceNotification {
    public static final int NOTIFICATION_ID = 1337;
    private static Notification notification;

    public static Notification getNotification(Context context) {
        if (notification == null) {
            notification = new NotificationCompat.Builder(context, CHANNEL_ID_ACTIVITY_RECORD)
                    .setContentTitle(context.getString(R.string.channelActivityRecordTitle))
                    .setContentText(context.getText(R.string.channelActivityRecordText))
                    .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                    .build();
        }

        return notification;
    }
}
