package de.rub.selab22a15.database.research;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accelerometer")
public class Accelerometer {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final Long timestamp;

    @ColumnInfo(name = "activity_timestamp")
    private final Long activityTimestamp;

    @ColumnInfo(name = "x")
    private final Float x;

    @ColumnInfo(name = "y")
    private final Float y;

    @ColumnInfo(name = "z")
    private final Float z;

    public Accelerometer(Long timestamp, Long activityTimestamp, Float x, Float y, Float z) {
        this.timestamp = timestamp;
        this.activityTimestamp = activityTimestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getActivityTimestamp() {
        return activityTimestamp;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public Float getZ() {
        return z;
    }
}