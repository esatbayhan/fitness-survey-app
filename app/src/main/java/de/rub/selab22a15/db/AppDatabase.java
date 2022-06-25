package de.rub.selab22a15.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.rub.selab22a15.R;

@Database(entities =
        {Accelerometer.class,
        Mood.class,
        GPS.class,
        EventAppraisal.class,
        Emotion.class,
        SocialContext.class,
        Activity.class}, version = 1, exportSchema = false)
abstract class AppDatabase extends RoomDatabase {
    abstract AccelerometerDao accelerometerDao();
    abstract MoodDao moodDao();
    abstract GPSDao gpsDao();
    abstract EventAppraisalDao eventAppraisalDao();
    abstract EmotionDao emotionDao();
    abstract SocialContextDao socialContextDao();
    abstract ActivityDao activityDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, context.getString(R.string.appDatabaseName))
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}