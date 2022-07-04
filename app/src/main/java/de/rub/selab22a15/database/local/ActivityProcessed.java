package de.rub.selab22a15.database.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_processed")
public class ActivityProcessed {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "weight")
    private final long weight;

    @ColumnInfo(name = "rating")
    private final float rating;

    public ActivityProcessed(long timestamp, long weight, float rating) {
        this.timestamp = timestamp;
        this.weight = weight;
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getWeight() {
        return weight;
    }

    public float getRating() {
        return rating;
    }
}
