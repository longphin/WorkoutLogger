package com.longlife.workoutlogger.enums;

public enum MeasurementType {
    REP(0),
    DURATION(1);

    private Integer _value;

    MeasurementType(Integer val) {
        this._value = val;
    }

    public static MeasurementType fromInt(Integer i) {
        if(i == null) return(null);

        for (MeasurementType et : MeasurementType.values()) {
            if (et.asInt() == i) {
                return (et);
            }
        }
        return (null);
    }

    public Integer asInt() {
        return Integer.valueOf(_value);
    }
}
