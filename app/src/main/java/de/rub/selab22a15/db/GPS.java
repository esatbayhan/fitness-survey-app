package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gps")
public class GPS {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "longitude")
    private final float longitude;

    @ColumnInfo(name = "latitude")
    private final float latitude;

    @ColumnInfo(name = "altitude")
    private final float altitude;

    @ColumnInfo(name = "velocity")
    private final float velocity;

    public GPS(long timestamp, float latitude, float longitude, float altitude, float velocity) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.velocity = velocity;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public float getVelocity() {
        return velocity;
    }
}
