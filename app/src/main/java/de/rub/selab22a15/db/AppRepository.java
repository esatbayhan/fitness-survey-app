package de.rub.selab22a15.db;

import android.content.Context;

public class AppRepository {
    public static void clearDatabase(Context context) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                AppDatabase.getDatabase(context).clearAllTables());
    }
}