package de.rub.selab22a15.database.research;

import android.app.Application;

public class SurveyRepository {
    private final SurveyDao surveyDao;

    public SurveyRepository(Application application) {
        ResearchDatabase db = ResearchDatabase.getDatabase(application);
        surveyDao = db.surveyDao();
    }

    public void insert(Survey survey) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                surveyDao.insert(survey));
    }
}
