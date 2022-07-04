package de.rub.selab22a15.database.research;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RuminationDao {
    @Query("SELECT * FROM rumination")
    LiveData<List<Rumination>> getRuminationData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Rumination rumination);

    @Query("DELETE FROM rumination")
    void delete();
}