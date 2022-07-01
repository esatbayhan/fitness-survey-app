package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RuminationRepository {
    private final RuminationDao ruminationDao;
    private final LiveData<List<Rumination>> allRumination;

    public RuminationRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        ruminationDao = db.ruminationDao();
        allRumination = ruminationDao.getRuminationData();
    }

    public LiveData<List<Rumination>> getAllRumination() {
        return allRumination;
    }

    public void insert(Rumination rumination) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                ruminationDao.insert(rumination));
    }

    public void delete() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                ruminationDao.delete());
    }
}