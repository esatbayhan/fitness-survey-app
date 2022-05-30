package de.rub.selab22a15.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GPSDao {
    @Query("SELECT * FROM gps")
    LiveData<List<GPS>> getGPSData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GPS gps);

    @Query("DELETE FROM gps")
    void deleteAll();
}