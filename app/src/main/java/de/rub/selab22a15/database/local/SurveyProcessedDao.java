package de.rub.selab22a15.database.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Collection;
import java.util.List;

@Dao
public interface SurveyProcessedDao {

    @Query("SELECT * FROM survey_processed")
    List<SurveyProcessed> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Collection<SurveyProcessed> surveyProcessedCollection);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SurveyProcessed surveyProcessed);
}
