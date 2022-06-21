package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EmotionRepository {
    private final EmotionDao emotionDao;
    private final LiveData<List<Emotion>> allEmotion;

    public EmotionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        emotionDao = db.emotionDao();
        allEmotion = emotionDao.getEmotionData();
    }

    public LiveData<List<Emotion>> getAllEmotion() {
        return allEmotion;
    }

    public void insert(Emotion emotion) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                emotionDao.insert(emotion));
    }

    public void delete() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                emotionDao.delete());
    }
}