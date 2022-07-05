package de.rub.selab22a15.database.research;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accelerometer")
public class Accelerometer {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "activity_timestamp")
    private final Long activityTimestamp;

    @ColumnInfo(name = "x")
    private final float x;

    @ColumnInfo(name = "y")
    private final float y;

    @ColumnInfo(name = "z")
    private final float z;

    public Accelerometer(long timestamp, Long activityTimestamp, float x, float y, float z) {
        this.timestamp = timestamp;
        this.activityTimestamp = activityTimestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Long getActivityTimestamp() {
        return activityTimestamp;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}