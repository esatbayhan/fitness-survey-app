package de.rub.selab22a15.database.research;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities =
        {Accelerometer.class,
                GPS.class,
                Activity.class,
                Survey.class,
                Rumination.class}, version = 1, exportSchema = false)
public abstract class ResearchDatabase extends RoomDatabase {
    abstract AccelerometerDao accelerometerDao();

    abstract GPSDao gpsDao();

    abstract ActivityDao activityDao();

    abstract SurveyDao surveyDao();

    abstract RuminationDao ruminationDao();

    public static final String DATABASE_NAME_RESEARCH = "research.db";
    private static volatile ResearchDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ResearchDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ResearchDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ResearchDatabase.class, DATABASE_NAME_RESEARCH)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}