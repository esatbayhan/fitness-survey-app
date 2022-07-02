package de.rub.selab22a15.workers;

import static de.rub.selab22a15.SettingsFragment.BATTERY_USAGE_HIGH;
import static de.rub.selab22a15.SettingsFragment.BATTERY_USAGE_LOW;
import static de.rub.selab22a15.SettingsFragment.BATTERY_USAGE_MEDIUM;
import static de.rub.selab22a15.SettingsFragment.KEY_BATTERY;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import de.rub.selab22a15.App;
import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.helpers.AccelerometerEventListener;
import de.rub.selab22a15.helpers.AccelerometerWriter;

public class AccelerometerRecordWorker extends Worker implements AccelerometerWriter {
    private static final long LOW = 50000;
    private static final long MEDIUM = 100000;
    private static final long HIGH = 150000;
    private static long SLEEP_MS = LOW;

    private static final String LOG_TAG = "ACCELEROMETER_RECORD_WORKER";
    private static final String TAG_PASSIVE_RECORDING = "PASSIVE_RECORDING";

    private final SensorManager sensorManager;
    private final Sensor sensorAccelerometer;
    private final AccelerometerEventListener accelerometerEventListener;
    private AccelerometerRepository accelerometerRepository;

    public static void start(Context context) {
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
    }

    public static void setBatteryUsage(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String batteryMode = sharedPreferences.getString(KEY_BATTERY, null);

        if (batteryMode == null) {
            Log.e(LOG_TAG, "batteryMode is not valid. Got: " + batteryMode);
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
            default:
        }

        Log.d(LOG_TAG, "Set battery usage to: " + SLEEP_MS);
    }

    public AccelerometerRecordWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerEventListener = new AccelerometerEventListener(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        accelerometerRepository = new AccelerometerRepository(App.getInstance());
        sensorManager.registerListener(accelerometerEventListener, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        try {
            Thread.sleep(SLEEP_MS);
            onStopped();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        sensorManager.unregisterListener(accelerometerEventListener);
    }

    @Override
    public void write(Accelerometer accelerometer) {
        accelerometerRepository.insert(accelerometer);
    }
}
