package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "eventAppraisal")
public class EventAppraisal {
    @Ignore
    private final int MINIMUM = 0;
    @Ignore
    private final int MAXIMUM = 100;

    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "intensity_negative_event")
    private final int intensityNegativeEvent;

    @ColumnInfo(name = "intensity_positive_event")
    private final int intensityPositiveEvent;

    private int bound(int x) {
        return bound(x, MINIMUM, MAXIMUM);
    }

    private int bound(int x, int mn, int mx) {
        if (x < mn) {
            return mn;
        }

        return Math.min(x, mx);
    }

    public EventAppraisal
            (long timestamp, int intensityNegativeEvent, int intensityPositiveEvent) {
        this.timestamp = timestamp;
        this.intensityNegativeEvent = bound(intensityNegativeEvent);
        this.intensityPositiveEvent = bound(intensityPositiveEvent);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getIntensityNegativeEvent() {
        return intensityNegativeEvent;
    }

    public int getIntensityPositiveEvent() {
        return intensityPositiveEvent;
    }
}
