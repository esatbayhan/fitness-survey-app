package de.rub.selab22a15.database.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ActivityProcessed.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    abstract ActivityProcessedDao activityProcessedDao();

    public static final String DATABASE_NAME_LOCAL = "local.db";
    private static volatile LocalDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static LocalDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    LocalDatabase.class, DATABASE_NAME_LOCAL)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}