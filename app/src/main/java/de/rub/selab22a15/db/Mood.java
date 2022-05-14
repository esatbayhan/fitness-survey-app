package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mood")
public class Mood {
    @Ignore
    private final int MINIMUM = 0;
    @Ignore
    private final int MAXIMUM = 100;

    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "satisfied")
    private final int satisfied;

    @ColumnInfo(name = "calm")
    private final int calm;

    @ColumnInfo(name = "well")
    private final int well;

    @ColumnInfo(name = "relaxed")
    private final int relaxed;

    @ColumnInfo(name = "energetic")
    private final int energetic;

    @ColumnInfo(name = "awake")
    private final int awake;

    private int bound(int x) {
        return bound(x, MINIMUM, MAXIMUM);
    }

    private int bound(int x, int mn, int mx) {
        if (x < mn) {
            return mn;
        }

        return Math.min(x, mx);
    }

    public Mood
            (long timestamp, int satisfied, int calm,
             int well, int relaxed, int energetic, int awake) {
        this.timestamp = timestamp;
        this.satisfied = bound(satisfied);
        this.calm = bound(calm);
        this.well = bound(well);
        this.relaxed = bound(relaxed);
        this.energetic = bound(energetic);
        this.awake = bound(awake);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getSatisfied() {
        return satisfied;
    }

    public int getCalm() {
        return calm;
    }

    public int getWell() {
        return well;
    }

    public int getRelaxed() {
        return relaxed;
    }

    public int getEnergetic() {
        return energetic;
    }

    public int getAwake() {
        return awake;
    }
}
