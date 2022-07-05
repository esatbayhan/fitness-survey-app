package de.rub.selab22a15.database.research;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AccelerometerRepository {
    private final AccelerometerDao accelerometerDao;

    public AccelerometerRepository(Application application) {
        ResearchDatabase db = ResearchDatabase.getDatabase(application);
        accelerometerDao = db.accelerometerDao();
    }

    // ATTENTION THIS SHOULD NOT BE USED ON THE MAIN THREAD!!!
    public List<Accelerometer> getAll() {
        return accelerometerDao.getAll();
    }

    public void insert(Accelerometer accelerometer) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.insert(accelerometer));
    }

    public void delete() {
        ResearchDatabase.databaseWriteExecutor.execute(accelerometerDao::delete);
    }

    public void delete(Long activityTimestamp) {
        if (activityTimestamp == null) {
            return;
        }

        ResearchDatabase.databaseWriteExecutor.execute(() ->
                accelerometerDao.delete(activityTimestamp));
    }

    public static float getLength(Accelerometer accelerometer) {
        float x = accelerometer.getX();
        float y = accelerometer.getY();
        float z = accelerometer.getZ();

        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public List<Accelerometer> getSinceUnsafe(long timestamp) {
        return accelerometerDao.getSince(timestamp);
    }
}