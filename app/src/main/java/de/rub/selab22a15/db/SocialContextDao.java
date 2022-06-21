package de.rub.selab22a15.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SocialContextDao {
    @Query("SELECT * FROM social_context")
    LiveData<List<SocialContext>> getSocialContextData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SocialContext socialContext);

    @Query("DELETE FROM social_context")
    void delete();
}