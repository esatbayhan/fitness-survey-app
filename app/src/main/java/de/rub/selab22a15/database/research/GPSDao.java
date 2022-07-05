package de.rub.selab22a15.database.research;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface GPSDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GPS gps);

    @Query("DELETE FROM gps")
    void deleteAll();

    @Query("DELETE FROM gps WHERE activity_timestamp = :activityTimestamp")
    void delete(long activityTimestamp);
}