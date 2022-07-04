package de.rub.selab22a15.receivers;

import static de.rub.selab22a15.SettingsFragment.DEFAULT_SURVEY_SCHEDULE;
import static de.rub.selab22a15.SettingsFragment.KEY_SURVEY_SCHEDULE;
import static de.rub.selab22a15.SettingsFragment.SURVEY_SCHEDULE_AFTERNOON;
import static de.rub.selab22a15.SettingsFragment.SURVEY_SCHEDULE_EVENING;
import static de.rub.selab22a15.SettingsFragment.SURVEY_SCHEDULE_MORNING;
import static de.rub.selab22a15.SettingsFragment.SURVEY_SCHEDULE_NIGHT;
import static de.rub.selab22a15.activities.SurveyActivity.EXTRA_FROM_NOTIFICATION;

import android.app.AlarmManager;
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
import androidx.core.app.TaskStackBuilder;
import androidx.preference.PreferenceManager;

import java.util.Calendar;

import de.rub.selab22a15.R;
import de.rub.selab22a15.activities.SurveyActivity;

public class SurveyAlarmReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "SURVEY_ALARM_RECEIVER";
    public static final String CHANNEL_ID_PERIODIC_NOTIFICATION = "PERIODIC_NOTIFICATION";
    public static final String CHANNEL_NAME_PERIODIC_NOTIFICATION = CHANNEL_ID_PERIODIC_NOTIFICATION;
    public static final int NOTIFICATION_ID_PERIODIC_NOTIFICATION = 2;

    private static final int MORNING_HOUR = 8;
    private static final int AFTERNOON_HOUR = 12;
    private static final int EVENING_HOUR = 18;
    private static final int NIGHT_HOUR = 21;

    public static void setAlarm(Context context) {
        int hour;
        String schedule = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_SURVEY_SCHEDULE, DEFAULT_SURVEY_SCHEDULE);

        switch (schedule) {
            case SURVEY_SCHEDULE_MORNING:
                hour = MORNING_HOUR;
                break;
            case SURVEY_SCHEDULE_AFTERNOON:
                hour = AFTERNOON_HOUR;
                break;
            case SURVEY_SCHEDULE_EVENING:
                hour = EVENING_HOUR;
                break;
            case SURVEY_SCHEDULE_NIGHT:
                hour = NIGHT_HOUR;
                break;
            default:
                Log.w(LOG_TAG, "Invalid schedule time. Got: " + schedule);
                return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent alarmIntent = new Intent(context, SurveyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
        Log.d(LOG_TAG, "SET ALARM FOR :" + calendar.getTimeInMillis());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "INSIDE onReceive(Context, Intent)");

        createNotificationChannel(context);
        Notification surveyNotification = createNotification(context);
        sendNotification(context, surveyNotification);
    }

    private static void createNotificationChannel(Context context) {
        Log.d(LOG_TAG, "INSIDE createNotificationChannel(Context)");

        NotificationChannel periodicNotificationNotificationChannel = new NotificationChannel(
                CHANNEL_ID_PERIODIC_NOTIFICATION,
                CHANNEL_NAME_PERIODIC_NOTIFICATION,
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(periodicNotificationNotificationChannel);
    }

    private static Notification createNotification(Context context) {
        Intent intent = new Intent(context, SurveyActivity.class);
        intent.putExtra(EXTRA_FROM_NOTIFICATION, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(
                context, CHANNEL_ID_PERIODIC_NOTIFICATION)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentTitle(context.getString(R.string.stringSurvey))
                .setContentText(context.getString(R.string.periodicNotificationText))
                .setContentIntent(pendingIntent)
                .build();
    }

    private static void sendNotification(Context context, Notification notification) {
        NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_PERIODIC_NOTIFICATION, notification);
    }

}
