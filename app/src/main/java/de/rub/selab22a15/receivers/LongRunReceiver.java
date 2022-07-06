package de.rub.selab22a15.receivers;

import static de.rub.selab22a15.MainActivity.EXTRA_FRAGMENT;
import static de.rub.selab22a15.MainActivity.EXTRA_FRAGMENT_ACTIVITY;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import de.rub.selab22a15.MainActivity;
import de.rub.selab22a15.R;

public class LongRunReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "LONG_RUN_RECEIVER";
    private static final String CHANNEL_ID = "LONG_RUN_NOTIFICATION";
    private static final String CHANNEL_NAME = CHANNEL_ID;
    private static final int NOTIFICATION_ID = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Inside onReceive(Context, Intent)");

        createNotificationChannel(context);
        Notification notification = createNotification(context);
        sendNotification(context, notification);
    }

    private static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    private static Notification createNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_FRAGMENT, EXTRA_FRAGMENT_ACTIVITY);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentTitle("Active Activity Recording")
                .setContentText("Are you still recording your activity?")
                .setContentIntent(pendingIntent)
                .build();
    }

    private static void sendNotification(Context context, Notification notification) {
        NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, notification);
    }
}
