package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ActivityRepository {
    private final ActivityDao activityDao;
    private final LiveData<List<Activity>> allActivity;

    public ActivityRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        activityDao = db.activityDao();
        allActivity = activityDao.getActivityData();
    }

    public LiveData<List<Activity>> getAllActivity() {
        return allActivity;
    }

    public void insert(Activity activity) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                activityDao.insert(activity));
    }

    public void delete() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                activityDao.delete());
    }
}