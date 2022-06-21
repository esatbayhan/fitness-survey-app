package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "emotion")
public class Emotion {
    @Ignore
    private final int MINIMUM = 0;
    @Ignore
    private final int MAXIMUM = 9;

    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "satisfied")
    private final int satisfied;

    @ColumnInfo(name = "failure")
    private final int failure;

    @ColumnInfo(name = "impulsiveness")
    private final int impulsiveness;

    @ColumnInfo(name = "aggressiveness")
    private final int aggressiveness;

    private int bound(int x) {
        return bound(x, MINIMUM, MAXIMUM);
    }

    private int bound(int x, int mn, int mx) {
        if (x < mn) {
            return mn;
        }

        return Math.min(x, mx);
    }

    public Emotion
            (long timestamp, int satisfied, int failure, int impulsiveness, int aggressiveness) {
        this.timestamp = timestamp;
        this.satisfied = bound(satisfied);
        this.failure = bound(failure);
        this.impulsiveness = bound(impulsiveness);
        this.aggressiveness = bound(aggressiveness);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getSatisfied() {
        return satisfied;
    }

    public int getFailure() {
        return failure;
    }

    public int getImpulsiveness() {
        return impulsiveness;
    }

    public int getAggressiveness() {
        return aggressiveness;
    }
}
