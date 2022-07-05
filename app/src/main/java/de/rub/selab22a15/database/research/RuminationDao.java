package de.rub.selab22a15.database.research;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RuminationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Rumination rumination);

    @Query("DELETE FROM rumination")
    void delete();
}