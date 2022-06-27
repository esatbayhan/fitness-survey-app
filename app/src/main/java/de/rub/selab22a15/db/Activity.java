package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity")
public class Activity {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final Long timestamp;

    @ColumnInfo(name = "activity")
    private String activity;

    public Activity(long timestamp, String activity) {
        this.timestamp = timestamp;
        this.activity = activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getActivity() {
        return activity;
    }
}