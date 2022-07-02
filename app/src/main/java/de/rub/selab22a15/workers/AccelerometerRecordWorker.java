package de.rub.selab22a15.workers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
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
    private static final String TAG_PASSIVE_RECORDING = "PASSIVE_RECORDING";
    private static final long SLEEP_MS = 50000;

    private final SensorManager sensorManager;
    private final Sensor sensorAccelerometer;
    private final AccelerometerEventListener accelerometerEventListener;
    private AccelerometerRepository accelerometerRepository;

    public static void start(Context context) {
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
