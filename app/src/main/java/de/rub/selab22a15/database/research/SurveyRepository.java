package de.rub.selab22a15.database.research;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

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

    public List<Survey> getAllUnsafe() {
        return surveyDao.getAll();
    }

    public static float getRating(Survey survey) {
        float rating = 0;

        Pair<Integer, Float> moodPair = getRatingMood(survey);
        Pair<Integer, Float> eventAppraisalSocialContextPair = getRatingEventAppraisalSocialContext(survey);
        Pair<Integer, Float> selfWorthPair = getRatingSelfWorth(survey);
        Pair<Integer, Float> impulsivenessPair = getRatingImpulsiveness(survey);

        rating += moodPair.second;
        rating += eventAppraisalSocialContextPair.second;
        rating += selfWorthPair.second;
        rating += impulsivenessPair.second;

        rating /= moodPair.first + eventAppraisalSocialContextPair.first + selfWorthPair.first +
                impulsivenessPair.first;

        return rating;
    }

    private static Pair<Integer, Float> getRatingMood(Survey survey) {
        float max = 100f;
        float rating = 0f;
        int counter = 0;

        Integer[] mood = {
                survey.getSatisfied(),
                survey.getCalm(),
                survey.getWell(),
                survey.getRelaxed(),
                survey.getEnergetic(),
                survey.getAwake()};

        for (Integer value : mood) {
            if (value == null) {
                continue;
            }

            rating += value / max;
            counter++;
        }

        return new Pair<>(counter, rating);
    }

    private static Pair<Integer, Float> getRatingEventAppraisalSocialContext(Survey survey) {
        float max = 100f;
        int counter = 0;
        float rating = 0f;

        // Event Appraisal
        if (survey.getNegativeEvent() != null) {
            rating += 1 - survey.getNegativeEvent() / max;
            counter++;
        }

        if (survey.getPositiveEvent() != null) {
            rating += survey.getPositiveEvent() / max;
            counter++;
        }

        // Social Context
        if (survey.getSurroundedLike() != null) {
            rating += 1 - survey.getSurroundedLike() / max;
            counter++;
        }

        return new Pair<>(counter, rating);
    }

    private static Pair<Integer, Float> getRatingSelfWorth(Survey survey) {
        float max = 9f;
        int counter = 0;
        float rating = 0f;

        if (survey.getNegativeSelfWorth() != null) {
            rating += 1 - survey.getNegativeSelfWorth() / max;
            counter++;
        }

        if (survey.getPositiveSelfWorth() != null) {
            rating += survey.getPositiveSelfWorth() / max;
            counter++;
        }

        return new Pair<>(counter, rating);
    }

    private static Pair<Integer, Float> getRatingImpulsiveness(Survey survey) {
        float max = 6;
        int counter = 0;
        float rating = 0;

        if (survey.getActedOnImpulse() != null) {
            rating += 1 - survey.getActedOnImpulse() / max;
            counter++;
        }

        if (survey.getActedAggressive() != null) {
            rating += 1 - survey.getActedAggressive() / max;
            counter++;
        }

        return new Pair<>(counter, rating);
    }
}
