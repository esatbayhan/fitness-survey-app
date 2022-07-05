package de.rub.selab22a15.database.research;

import android.app.Application;

public class ActivityRepository {
    private final ActivityDao activityDao;

    public ActivityRepository(Application application) {
        ResearchDatabase db = ResearchDatabase.getInstance(application);
        activityDao = db.activityDao();
    }

    public void insert(Activity activity) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                activityDao.insert(activity));
    }

    public void delete() {
        ResearchDatabase.databaseWriteExecutor.execute(activityDao::delete);
    }
}