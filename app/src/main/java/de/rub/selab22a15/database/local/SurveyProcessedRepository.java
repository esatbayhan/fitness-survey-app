package de.rub.selab22a15.database.local;

import android.app.Application;

import java.util.Collection;
import java.util.List;

public class SurveyProcessedRepository {
    private final SurveyProcessedDao surveyProcessedDao;

    public SurveyProcessedRepository(Application application) {
        LocalDatabase database = LocalDatabase.getInstance(application);
        surveyProcessedDao = database.surveyProcessedDao();
    }

    public List<SurveyProcessed> getAllUnsafe() {
        return surveyProcessedDao.getAll();
    }

    public List<SurveyProcessed> getRangeUnsafe(long start, long end) {
        return surveyProcessedDao.getRange(start, end);
    }

    public void insert(SurveyProcessed surveyProcessed) {
        LocalDatabase.databaseWriteExecutor.execute(() ->
                surveyProcessedDao.insert(surveyProcessed));
    }

    public void insert(Collection<SurveyProcessed> surveyProcessedCollection) {
        LocalDatabase.databaseWriteExecutor.execute(() ->
                surveyProcessedDao.insert(surveyProcessedCollection));
    }
}