package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "social_context")
public class SocialContext {
    @Ignore
    private final int MINIMUM = -1;
    @Ignore
    private final int MAXIMUM = 100;

    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "place")
    private final int place;

    @ColumnInfo(name = "alone")
    private final boolean alone;

    @ColumnInfo(name = "surrounded")
    private final int surrounded;
    private int bound(int x) {
        return bound(x, MINIMUM, MAXIMUM);
    }

    @ColumnInfo(name = "like")
    private final int like;

    private int bound(int x, int mn, int mx) {
        if (x < mn) {
            return mn;
        }

        return Math.min(x, mx);
    }

    public SocialContext
            (long timestamp, int place, boolean alone, int surrounded, int like) {
        this.timestamp = timestamp;
        this.place = bound(place);
        this.alone = alone;
        this.surrounded = bound(surrounded);
        this.like = bound(like);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPlace() {
        return place;
    }

    public boolean isAlone() {
        return alone;
    }

    public int getSurrounded() {
        return surrounded;
    }

    public int getLike() {
        return like;
    }
}
