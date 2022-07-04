package de.rub.selab22a15.database.research;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AccelerometerRepository {
    private final AccelerometerDao accelerometerDao;
    private final LiveData<List<Accelerometer>> allAccelerometer;

    public AccelerometerRepository(Application application) {
        ResearchDatabase db = ResearchDatabase.getDatabase(application);
        accelerometerDao = db.accelerometerDao();
        allAccelerometer = accelerometerDao.getAccelerometerData();
    }

    public LiveData<List<Accelerometer>> getAllAccelerometer() {
        return allAccelerometer;
    }

    public void insert(Accelerometer accelerometer) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.insert(accelerometer));
    }

    public void delete() {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.delete());
    }

    public void delete(Long activityTimestamp) {
        if (activityTimestamp == null) {
            return;
        }

        ResearchDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.delete(activityTimestamp));
    }
}