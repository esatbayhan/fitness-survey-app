package de.rub.selab22a15.database.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "survey_processed")
public class SurveyProcessed {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "rating")
    private final float rating;

    public SurveyProcessed(long timestamp, float rating) {
        this.timestamp = timestamp;
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getRating() {
        return rating;
    }
}
