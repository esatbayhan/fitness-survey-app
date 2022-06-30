package de.rub.selab22a15.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "survey")
public class Survey {
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private final long timestamp;

    @ColumnInfo(name = "activity_timestamp")
    private final Long activityTimestamp;

    // Mood Survey
    @ColumnInfo(name = "satisfied")
    private final Integer satisfied;

    @ColumnInfo(name = "calm")
    private final Integer calm;

    @ColumnInfo(name = "well")
    private final Integer well;

    @ColumnInfo(name = "relaxed")
    private final Integer relaxed;

    @ColumnInfo(name = "energetic")
    private final Integer energetic;

    @ColumnInfo(name = "awake")
    private final Integer awake;

    // EventAppraisal Survey
    @ColumnInfo(name = "negative_event")
    private final Integer negativeEvent;

    @ColumnInfo(name = "positive_event")
    private final Integer positiveEvent;

    // Self Worth Survey
    @ColumnInfo(name = "negative_self_worth")
    private final Integer negativeSelfWorth;

    @ColumnInfo(name = "positive_self_worth")
    private final Integer positiveSelfWorth;

    // Impulsiveness Survey
    @ColumnInfo(name = "acted_on_impulse")
    private final Integer actedOnImpulse;

    @ColumnInfo(name = "acted_aggressive")
    private final Integer actedAggressive;

    // Social Context
    @ColumnInfo(name = "location")
    private final String location;

    @ColumnInfo(name = "surrounded")
    private final String surrounded;

    @ColumnInfo(name = "is_alone")
    private final Boolean isAlone;

    @ColumnInfo(name = "surrounded_like")
    private final Integer surroundedLike;

    @ColumnInfo(name = "note")
    private final String note;

    public Survey(
            long timestamp,
            Long activityTimestamp,
            Integer satisfied,
            Integer calm,
            Integer well,
            Integer relaxed,
            Integer energetic,
            Integer awake,
            Integer negativeEvent,
            Integer positiveEvent,
            Integer negativeSelfWorth,
            Integer positiveSelfWorth,
            Integer actedOnImpulse,
            Integer actedAggressive,
            String location,
            String surrounded,
            Boolean isAlone,
            Integer surroundedLike,
            String note) {
        this.timestamp = timestamp;
        this.activityTimestamp = activityTimestamp;
        this.satisfied = satisfied;
        this.calm = calm;
        this.well = well;
        this.relaxed = relaxed;
        this.energetic = energetic;
        this.awake = awake;
        this.negativeEvent = negativeEvent;
        this.positiveEvent = positiveEvent;
        this.negativeSelfWorth = negativeSelfWorth;
        this.positiveSelfWorth = positiveSelfWorth;
        this.actedOnImpulse = actedOnImpulse;
        this.actedAggressive = actedAggressive;
        this.location = location;
        this.surrounded = surrounded;
        this.isAlone = isAlone;
        this.surroundedLike = surroundedLike;
        this.note = note;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Long getActivityTimestamp() {
        return activityTimestamp;
    }

    public Integer getSatisfied() {
        return satisfied;
    }

    public Integer getCalm() {
        return calm;
    }

    public Integer getWell() {
        return well;
    }

    public Integer getRelaxed() {
        return relaxed;
    }

    public Integer getEnergetic() {
        return energetic;
    }

    public Integer getAwake() {
        return awake;
    }

    public Integer getNegativeEvent() {
        return negativeEvent;
    }

    public Integer getPositiveEvent() {
        return positiveEvent;
    }

    public Integer getNegativeSelfWorth() {
        return negativeSelfWorth;
    }

    public Integer getPositiveSelfWorth() {
        return positiveSelfWorth;
    }

    public Integer getActedOnImpulse() {
        return actedOnImpulse;
    }

    public Integer getActedAggressive() {
        return actedAggressive;
    }

    public String getLocation() {
        return location;
    }

    public String getSurrounded() {
        return surrounded;
    }

    public Boolean getAlone() {
        return isAlone;
    }

    public Integer getSurroundedLike() {
        return surroundedLike;
    }

    public String getNote() {
        return note;
    }
}
