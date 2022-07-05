package de.rub.selab22a15.database.research;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AccelerometerDao {
    @Query("SELECT * FROM accelerometer")
    List<Accelerometer> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Accelerometer accelerometer);

    @Query("DELETE FROM accelerometer")
    void delete();

    @Query("DELETE FROM accelerometer WHERE activity_timestamp = :activityTimestamp")
    void delete(long activityTimestamp);

    @Query("SELECT * FROM accelerometer WHERE timestamp > :timestamp")
    List<Accelerometer> getSince(long timestamp);
}