package com.longlife.workoutlogger.enums;

public enum ExerciseType {
    WEIGHT(0),
    BODYWEIGHT(1),
    DISTANCE(2);

    private int _value;

    ExerciseType(int val) {
        this._value = val;
    }

    public static ExerciseType fromInt(int i) {
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