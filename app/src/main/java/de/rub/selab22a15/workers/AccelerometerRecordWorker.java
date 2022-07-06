package de.rub.selab22a15.workers;

import static de.rub.selab22a15.SettingsFragment.BATTERY_USAGE_HIGH;
import static de.rub.selab22a15.SettingsFragment.BATTERY_USAGE_LOW;
import static de.rub.selab22a15.SettingsFragment.BATTERY_USAGE_MEDIUM;
import static de.rub.selab22a15.SettingsFragment.KEY_BATTERY;
import static de.rub.selab22a15.SettingsFragment.KEY_PASSIVE_RECORDING;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import de.rub.selab22a15.services.AccelerometerRecordService;

public class AccelerometerRecordWorker extends Worker {
    private static final long LOW = 50000;
    private static final long MEDIUM = 100000;
    private static final long HIGH = 150000;
    private static long SLEEP_MS = LOW;

    private static final String LOG_TAG = "ACCELEROMETER_RECORD_WORKER";
    private static final String TAG_PASSIVE_RECORDING = "PASSIVE_RECORDING";

    public static void start(Context context) {
        Log.d(LOG_TAG, "Inside start(Context)");

        if (!hasPermissions(context)) {
            return;
        }

        setBatteryUsage(context);

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                AccelerometerRecordWorker.class,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .addTag(TAG_PASSIVE_RECORDING)
                .build();

        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                        TAG_PASSIVE_RECORDING,
                        ExistingPeriodicWorkPolicy.REPLACE,
                        workRequest
                );

        Log.d(LOG_TAG, "End of start(Context)");
    }

    public static void stop(Context context) {
        WorkManager.getInstance(context)
                .cancelUniqueWork(TAG_PASSIVE_RECORDING);
    }

    private static boolean hasPermissions(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(KEY_PASSIVE_RECORDING, false);
    }

    private static void setBatteryUsage(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String batteryMode = sharedPreferences.getString(KEY_BATTERY, null);

        if (batteryMode == null) {
            Log.e(LOG_TAG, "batteryMode is null");
            return;
        }

        switch (batteryMode) {
            case BATTERY_USAGE_LOW:
                SLEEP_MS = LOW;
                break;
            case BATTERY_USAGE_MEDIUM:
                SLEEP_MS = MEDIUM;
                break;
            case BATTERY_USAGE_HIGH:
                SLEEP_MS = HIGH;
                break;
        }

        Log.d(LOG_TAG, "Set battery usage to: " + SLEEP_MS);
    }

    public AccelerometerRecordWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(LOG_TAG, "Inside doWork()");

        if (AccelerometerRecordService.isActiveRecording()) {
            Log.d(LOG_TAG, "AccelerometerRecordService is currently active recording");
            return Result.success();
        }

        Intent passiveRecordingIntent = new Intent(
                getApplicationContext(), AccelerometerRecordService.class);
        ContextCompat.startForegroundService(getApplicationContext(), passiveRecordingIntent);

        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clear();

        Log.d(LOG_TAG, "Exiting doWork()");
        return Result.success();
    }

    @Override
    public void onStopped() {
        Log.d(LOG_TAG, "Inside onStopped()");

        super.onStopped();
        clear();
    }

    private void clear() {
        Log.d(LOG_TAG, "Inside clear()");

        if (AccelerometerRecordService.isActiveRecording()) {
            Log.d(LOG_TAG, "Inside Clear() -> skipped because is active recording");
            return;
        }

        getApplicationContext().stopService(new Intent(getApplicationContext(),
                AccelerometerRecordService.class));
    }
}
