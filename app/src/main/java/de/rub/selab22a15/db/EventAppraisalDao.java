package de.rub.selab22a15.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventAppraisalDao {
    @Query("SELECT * FROM eventAppraisal")
    LiveData<List<EventAppraisal>> getEventAppraisalData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(EventAppraisal eventAppraisal);

    @Query("DELETE FROM eventAppraisal")
    void delete();
}