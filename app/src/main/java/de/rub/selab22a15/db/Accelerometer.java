package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accelerometer")
public class Accelerometer {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "x")
    private final float x;

    @ColumnInfo(name = "y")
    private final float y;

    @ColumnInfo(name = "z")
    private final float z;

    public Accelerometer(long timestamp, float x, float y, float z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }
}