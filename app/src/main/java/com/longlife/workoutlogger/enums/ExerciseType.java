package com.longlife.workoutlogger.enums;

public enum ExerciseType {
    WEIGHT(0),
    BODYWEIGHT(1),
    DISTANCE(2);

    private Integer _value;

    ExerciseType(Integer val) {
        this._value = val;
    }

    public static ExerciseType fromInt(Integer i) {
        if (i == null)
            return (null);

        for (ExerciseType et : ExerciseType.values()) {
            if (et.asInt() == i) {
                return (et);
            }
        }
        return (null);
    }

    public int asInt() {
        return _value;
    }
}
