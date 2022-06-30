package de.rub.selab22a15.db;

import android.app.Application;

public class SurveyRepository {
    private final SurveyDao surveyDao;

    public SurveyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        surveyDao = db.surveyDao();
    }

    public void insert(Survey survey) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                surveyDao.insert(survey));
    }
}
