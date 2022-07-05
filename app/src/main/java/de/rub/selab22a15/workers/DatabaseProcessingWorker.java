package de.rub.selab22a15.workers;

import static de.rub.selab22a15.App.APPLICATION_PREFERENCES;
import static de.rub.selab22a15.App.KEY_LAST_TIME_PROCESSED;

import android.content.Context;
import android.content.SharedPreferences;
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

        long timestampUpTo = System.currentTimeMillis();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                APPLICATION_PREFERENCES, Context.MODE_PRIVATE);

        long timestampSince = sharedPreferences.getLong(KEY_LAST_TIME_PROCESSED, 0);

        activityProcessing(timestampSince);
        surveyProcessing(timestampSince);

        sharedPreferences.edit().putLong(KEY_LAST_TIME_PROCESSED, timestampUpTo).apply();

        return Result.success();
    }

    private static void activityProcessing(long timestamp) {
        /*Sums the length of the accelerometer vectors of one day */
        List<Accelerometer> accelerometerList = new AccelerometerRepository(App.getInstance())
                .getSinceUnsafe(timestamp);
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

    private static void surveyProcessing(long timestamp) {
        /*Maps all discrete survey values to a range of [0, 1]*/
        List<Survey> surveyList = new SurveyRepository(App.getInstance()).getSinceUnsafe(timestamp);
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
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
