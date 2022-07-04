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
    private float weight;

    @ColumnInfo(name = "rating")
    private float rating;

    public ActivityProcessed(long timestamp, float weight, float rating) {
        this.timestamp = timestamp;
        this.weight = weight;
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getWeight() {
        return weight;
    }

    public float getRating() {
        return rating;
    }

    public void addWeight(float weight) {
        this.weight += weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
