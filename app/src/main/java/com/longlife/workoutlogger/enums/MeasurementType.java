package com.longlife.workoutlogger.enums;

public enum MeasurementType {
    REP(0),
    DURATION(1);

    private int _value;

    MeasurementType(int val) {
        this._value = val;
    }

    public static MeasurementType fromLong(Long l) {
        for (MeasurementType et : MeasurementType.values()) {
            if (et.asLong() == l) {
                return (et);
            }
        }
        return (null);
    }

    public Long asLong() {
        return Long.valueOf(_value);
    }
}
