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

    @ColumnInfo(name = "like")
    private final int like;

    @ColumnInfo(name = "alone")
    private final boolean alone;

    @ColumnInfo(name = "surrounded")
    private final int surrounded;

    private int bound(int x) {
        return bound(x, MINIMUM, MAXIMUM);
    }

    private int bound(int x, int mn, int mx) {
        if (x < mn) {
            return mn;
        }

        return Math.min(x, mx);
    }

    public SocialContext
            (long timestamp, int like, boolean alone, int surrounded) {
        this.timestamp = timestamp;
        this.like = bound(like);
        this.alone = alone;
        this.surrounded = bound(surrounded);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getLike() {
        return like;
    }

    public boolean isAlone() {
        return alone;
    }

    public int getSurrounded() {
        return surrounded;
    }
}
