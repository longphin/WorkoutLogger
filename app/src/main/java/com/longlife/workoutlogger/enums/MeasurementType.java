package com.longlife.workoutlogger.enums;

public enum MeasurementType {
    REP(0),
    DURATION(1);

    private int _value;

    MeasurementType(int val) {
        this._value = val;
    }

    public static MeasurementType fromInt(int i) {
        for (MeasurementType et : MeasurementType.values()) {
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
