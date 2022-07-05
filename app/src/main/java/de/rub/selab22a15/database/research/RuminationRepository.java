package de.rub.selab22a15.database.research;

import android.app.Application;

public class RuminationRepository {
    private final RuminationDao ruminationDao;

    public RuminationRepository(Application application) {
        ResearchDatabase db = ResearchDatabase.getInstance(application);
        ruminationDao = db.ruminationDao();
    }

    public void insert(Rumination rumination) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                ruminationDao.insert(rumination));
    }

    public void delete() {
        ResearchDatabase.databaseWriteExecutor.execute(ruminationDao::delete);
    }
}