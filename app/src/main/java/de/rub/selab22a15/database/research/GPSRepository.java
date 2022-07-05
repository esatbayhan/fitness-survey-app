package de.rub.selab22a15.database.research;

import android.app.Application;

public class GPSRepository {
    private final GPSDao gpsDao;

    public GPSRepository(Application application) {
        ResearchDatabase db = ResearchDatabase.getInstance(application);
        gpsDao = db.gpsDao();
    }

    public void insert(GPS gps) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                gpsDao.insert(gps));
    }

    public void delete(Long activityTimestamp) {
        if (activityTimestamp == null) {
            return;
        }

        ResearchDatabase.databaseWriteExecutor.execute(() ->
                gpsDao.delete(activityTimestamp));
    }

    public void deleteAll() {
        ResearchDatabase.databaseWriteExecutor.execute(gpsDao::deleteAll);
    }
}