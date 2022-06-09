package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GPSRepository {
    private final GPSDao gpsDao;
    private final LiveData<List<GPS>> allGPS;

    public GPSRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        gpsDao = db.gpsDao();
        allGPS = gpsDao.getGPSData();
    }

    public LiveData<List<GPS>> getAllGPS() {
        return allGPS;
    }

    public void insert(GPS gps) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                gpsDao.insert(gps));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                gpsDao.deleteAll());
    }
}