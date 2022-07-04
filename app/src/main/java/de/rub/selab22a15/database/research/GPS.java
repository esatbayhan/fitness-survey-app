package de.rub.selab22a15.database.research;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gps")
public class GPS {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "activity_timestamp")
    private final Long activityTimestamp;

    @ColumnInfo(name = "longitude")
    private final Double longitude;

    @ColumnInfo(name = "latitude")
    private final Double latitude;

    @ColumnInfo(name = "altitude")
    private final Double altitude;

    @ColumnInfo(name = "accuracy")
    private final Float accuracy;

    @ColumnInfo(name = "velocity")
    private final Float speed;

    public GPS(
            long timestamp,
            long activityTimestamp,
            Double latitude,
            Double longitude,
            Double altitude,
            Float accuracy,
            Float speed) {
        this.timestamp = timestamp;
        this.activityTimestamp = activityTimestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Long getActivityTimestamp() {
        return activityTimestamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public Float getSpeed() {
        return speed;
    }
}
