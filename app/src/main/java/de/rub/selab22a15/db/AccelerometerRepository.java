package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AccelerometerRepository {
    private final AccelerometerDao accelerometerDao;
    private final LiveData<List<Accelerometer>> allAccelerometer;

    public AccelerometerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        accelerometerDao = db.accelerometerDao();
        allAccelerometer = accelerometerDao.getAccelerometerData();
    }

    public LiveData<List<Accelerometer>> getAllAccelerometer() {
        return allAccelerometer;
    }

    public void insert(Accelerometer accelerometer) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.insert(accelerometer));
    }

    public void delete() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.delete());
    }

    public void delete(Long activityTimestamp) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.delete(activityTimestamp));
    }
}