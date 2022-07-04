package de.rub.selab22a15.database.research;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM activity")
    LiveData<List<Activity>> getActivityData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Activity activity);

    @Query("DELETE FROM activity")
    void delete();
}