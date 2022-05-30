package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MoodRepository {
    private final MoodDao moodDao;
    private final LiveData<List<Mood>> allMood;

    public MoodRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        moodDao = db.moodDao();
        allMood = moodDao.getMoodData();
    }

    public LiveData<List<Mood>> getAllMood() {
        return allMood;
    }

    public void insert(Mood mood) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                moodDao.insert(mood));
    }

    public void delete() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                moodDao.delete());
    }
}