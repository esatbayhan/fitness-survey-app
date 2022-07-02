package de.rub.selab22a15.workers;

import static de.rub.selab22a15.App.CHANNEL_ID_PERIODIC_NOTIFICATION;
import static de.rub.selab22a15.App.NOTIFICATION_ID_PERIODIC_NOTIFICATION;
import static de.rub.selab22a15.activities.SurveyActivity.EXTRA_FROM_NOTIFICATION;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import de.rub.selab22a15.R;
import de.rub.selab22a15.activities.SurveyActivity;

public class PeriodicNotificationWorker extends Worker {
    public PeriodicNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
        intent.putExtra(EXTRA_FROM_NOTIFICATION, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID_PERIODIC_NOTIFICATION)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentTitle(getApplicationContext().getString(R.string.stringSurvey))
                .setContentText(getApplicationContext().getString(R.string.periodicNotificationText))
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(
                getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID_PERIODIC_NOTIFICATION,
                notificationBuilder.build());

        return Result.success();
    }
}
