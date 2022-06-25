package de.rub.selab22a15.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AccelerometerDao {
    @Query("SELECT * FROM accelerometer")
    LiveData<List<Accelerometer>> getAccelerometerData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Accelerometer accelerometer);

    @Query("DELETE FROM accelerometer")
    void delete();

    @Query("DELETE FROM accelerometer WHERE activity_timestamp = :activityTimestamp")
    void delete(long activityTimestamp);
}