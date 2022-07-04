package de.rub.selab22a15.database.local;

import android.app.Application;

import java.util.List;

public class ActivityProcessedRepository {
    private final ActivityProcessedDao activityProcessedDao;

    public ActivityProcessedRepository(Application application) {
        LocalDatabase database = LocalDatabase.getInstance(application);
        activityProcessedDao = database.activityProcessedDao();
    }

    public void insert(ActivityProcessed activityProcessed) {
        LocalDatabase.databaseWriteExecutor.execute(() ->
                activityProcessedDao.insert(activityProcessed));
    }

    public void insert(List<ActivityProcessed> activityProcessed) {
        LocalDatabase.databaseWriteExecutor.execute(() ->
                activityProcessedDao.insert(activityProcessed));
    }
}