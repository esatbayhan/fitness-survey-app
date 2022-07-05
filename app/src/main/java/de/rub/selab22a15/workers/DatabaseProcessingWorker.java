package de.rub.selab22a15.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.rub.selab22a15.App;
import de.rub.selab22a15.database.local.ActivityProcessed;
import de.rub.selab22a15.database.local.ActivityProcessedRepository;
import de.rub.selab22a15.database.local.SurveyProcessed;
import de.rub.selab22a15.database.local.SurveyProcessedRepository;
import de.rub.selab22a15.database.research.Accelerometer;
import de.rub.selab22a15.database.research.AccelerometerRepository;
import de.rub.selab22a15.database.research.Survey;
import de.rub.selab22a15.database.research.SurveyRepository;

public class DatabaseProcessingWorker extends Worker {
    private static final String LOG_TAG = "DATABASE_PROCESSING_WORKER";

    public static void start(Context context) {
        Log.d(LOG_TAG, "Inside start(Context)");

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(DatabaseProcessingWorker.class)
                .build();

        WorkManager.getInstance(context)
                .enqueue(workRequest);
    }

    public DatabaseProcessingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        Log.d(LOG_TAG, "Inside DatabaseProcessingWorker(...)");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(LOG_TAG, "Inside doWork()");

        activityProcessing();
        surveyProcessing();

        return Result.success();
    }

    private static void activityProcessing() {
        /*Sums the length of the accelerometer vectors of one day */
        List<Accelerometer> accelerometerList = new AccelerometerRepository(App.getInstance())
                .getAll();
        Map<Long, ActivityProcessed> activityDateMap = new HashMap<>();

        for (Accelerometer accelerometer : accelerometerList) {
            long timestampDay = removeTime(accelerometer.getTimestamp());
            ActivityProcessed activityProcessed = activityDateMap.get(timestampDay);

            if (activityProcessed == null) {
                activityProcessed = new ActivityProcessed(timestampDay, 0, 0f);
                activityDateMap.put(timestampDay, activityProcessed);
            }

            activityProcessed.addWeight(AccelerometerRepository.getLength(accelerometer));
        }

        new ActivityProcessedRepository(App.getInstance()).insert(activityDateMap.values());
    }

    private static void surveyProcessing() {
        /*Maps all discrete survey values to a range of [0, 1]*/
        List<Survey> surveyList = new SurveyRepository(App.getInstance()).getAllUnsafe();
        List<SurveyProcessed> surveyProcessedList = new ArrayList<>();

        for (Survey survey : surveyList) {
            surveyProcessedList.add(
                    new SurveyProcessed(survey.getTimestamp(), SurveyRepository.getRating(survey)));
        }

        new SurveyProcessedRepository(App.getInstance()).insert(surveyProcessedList);
    }

    private static long removeTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
