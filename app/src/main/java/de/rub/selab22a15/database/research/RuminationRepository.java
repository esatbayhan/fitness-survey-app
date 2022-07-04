package de.rub.selab22a15.database.research;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RuminationRepository {
    private final RuminationDao ruminationDao;
    private final LiveData<List<Rumination>> allRumination;

    public RuminationRepository(Application application) {
        ResearchDatabase db = ResearchDatabase.getDatabase(application);
        ruminationDao = db.ruminationDao();
        allRumination = ruminationDao.getRuminationData();
    }

    public LiveData<List<Rumination>> getAllRumination() {
        return allRumination;
    }

    public void insert(Rumination rumination) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                ruminationDao.insert(rumination));
    }

    public void delete() {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                ruminationDao.delete());
    }
}