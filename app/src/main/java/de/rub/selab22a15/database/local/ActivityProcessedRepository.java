package de.rub.selab22a15.database.local;

import android.app.Application;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.rub.selab22a15.database.research.Accelerometer;

public class ActivityProcessedRepository {
    private final ActivityProcessedDao activityProcessedDao;

    public ActivityProcessedRepository(Application application) {
        LocalDatabase database = LocalDatabase.getInstance(application);
        activityProcessedDao = database.activityProcessedDao();
    }

    public List<ActivityProcessed> getAll() {
        return activityProcessedDao.getAll();
    }

    public void insert(ActivityProcessed activityProcessed) {
        LocalDatabase.databaseWriteExecutor.execute(() ->
                activityProcessedDao.insert(activityProcessed));
    }

    public void insert(Collection<ActivityProcessed> activityProcessed) {
        LocalDatabase.databaseWriteExecutor.execute(() ->
                activityProcessedDao.insert(activityProcessed));
    }

    public void processInsert(Collection<Accelerometer> accelerometerList) {

        LocalDatabase.databaseWriteExecutor.execute(() -> {
        });
    }

    private float getLength(Accelerometer accelerometer) {
        float x = accelerometer.getX();
        float y = accelerometer.getY();
        float z = accelerometer.getZ();

        return (float) Math.sqrt(x*x + y*y + z*z);
    }
    
    private long removeTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        return calendar.getTimeInMillis();
    }
}