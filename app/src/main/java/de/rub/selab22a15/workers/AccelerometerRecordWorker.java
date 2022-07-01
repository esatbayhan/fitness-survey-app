package de.rub.selab22a15.workers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import de.rub.selab22a15.App;
import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.helpers.AccelerometerEventListener;
import de.rub.selab22a15.helpers.AccelerometerWriter;

public class AccelerometerRecordWorker extends Worker implements AccelerometerWriter {

    private static final long SLEEP_MS = 50000;

    private final SensorManager sensorManager;
    private final Sensor sensorAccelerometer;
    private final AccelerometerEventListener accelerometerEventListener;

    private AccelerometerRepository accelerometerRepository;

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
    public void write(Accelerometer accelerometer) {
        accelerometerRepository.insert(accelerometer);
    }
}
