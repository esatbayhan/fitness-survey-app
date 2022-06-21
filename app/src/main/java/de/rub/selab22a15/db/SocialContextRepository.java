package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SocialContextRepository {
    private final SocialContextDao socialContextDao;
    private final LiveData<List<SocialContext>> allSocialContext;

    public SocialContextRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        socialContextDao = db.socialContextDao();
        allSocialContext = socialContextDao.getSocialContextData();
    }

    public LiveData<List<SocialContext>> getAllSocialContext() {
        return allSocialContext;
    }

    public void insert(SocialContext socialContext) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                socialContextDao.insert(socialContext));
    }

    public void delete() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                socialContextDao.delete());
    }
}