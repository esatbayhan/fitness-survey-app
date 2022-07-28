package de.rub.selab22a15.database.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Collection;
import java.util.List;

@Dao
public interface ActivityProcessedDao {
    /*
    @Query("SELECT * FROM activity_processed WHERE timestamp BETWEEN :timestampStart AND :timestampEnd")
    List<ActivityProcessed> getBetween(long timestampStart, long timestampEnd);
*/
    @Query("SELECT * FROM activity_processed WHERE timestamp >= :start AND timestamp <= :end")
    List<ActivityProcessed> getRange(long start, long end);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActivityProcessed activityProcessed);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Collection<ActivityProcessed> activityProcessed);

    @Query("SELECT * FROM activity_processed")
    List<ActivityProcessed> getAll();
}
