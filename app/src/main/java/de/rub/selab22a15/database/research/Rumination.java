package de.rub.selab22a15.database.research;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rumination")
public class Rumination {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final Long timestamp;

    @ColumnInfo(name = "activity_timestamp")
    private final Long activityTimestamp;

    @ColumnInfo(name = "reason")
    private final Integer reason;

    public Rumination(Long timestamp, Long activityTimestamp, Integer reason) {
        this.timestamp = timestamp;
        this.activityTimestamp = activityTimestamp;
        this.reason = reason;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getActivityTimestamp() {
        return activityTimestamp;
    }

    public Integer getReason() {
        return reason;
    }
}