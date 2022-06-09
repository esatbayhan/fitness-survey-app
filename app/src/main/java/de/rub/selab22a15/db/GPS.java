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
    private final double longitude;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    @ColumnInfo(name = "altitude")
    private final double altitude;

    @ColumnInfo(name = "accuracy")
    private final double accuracy;

    @ColumnInfo(name = "velocity")
    private final double speed;

    public GPS(long timestamp, double latitude, double longitude, double altitude, double accuracy, double speed) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getSpeed() {
        return speed;
    }
}
