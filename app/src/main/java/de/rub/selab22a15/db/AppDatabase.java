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
        GPS.class,
        Activity.class,
        Survey.class,
        Rumination.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    abstract AccelerometerDao accelerometerDao();
    abstract GPSDao gpsDao();
    abstract ActivityDao activityDao();
    abstract SurveyDao surveyDao();
    abstract RuminationDao ruminationDao();

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

    public static void destroyInstance() {
        if (INSTANCE == null) {
            return;
        }

        if (INSTANCE.isOpen()) {
            INSTANCE.close();
        }

        INSTANCE = null;
    }
}