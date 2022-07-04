package de.rub.selab22a15.database.research;

import android.content.Context;

public class ResearchRepository {
    public static void clearDatabase(Context context) {
        ResearchDatabase.databaseWriteExecutor.execute(() ->
                ResearchDatabase.getDatabase(context).clearAllTables());
    }
}