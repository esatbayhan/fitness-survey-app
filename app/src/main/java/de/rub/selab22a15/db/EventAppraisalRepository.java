package de.rub.selab22a15.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventAppraisalRepository {
    private final EventAppraisalDao eventAppraisalDao;
    private final LiveData<List<EventAppraisal>> allEventAppraisal;

    public EventAppraisalRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        eventAppraisalDao = db.eventAppraisalDao();
        allEventAppraisal = eventAppraisalDao.getEventAppraisalData();
    }

    public LiveData<List<EventAppraisal>> getAllEventAppraisal() {
        return allEventAppraisal;
    }

    public void insert(EventAppraisal eventAppraisal) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                eventAppraisalDao.insert(eventAppraisal));
    }

    public void delete() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                eventAppraisalDao.delete());
    }
}