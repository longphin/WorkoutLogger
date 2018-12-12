/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.enums;

public enum MeasurementType {
    REP(0),
    DURATION(1);

    private Integer _value;

    MeasurementType(Integer val) {
        this._value = val;
    }

    public static MeasurementType fromInt(Integer i) {
        if (i == null)
            return (null);

        for (MeasurementType val : MeasurementType.values()) {
            if (val.asInt().equals(i)) {
                return (val);
            }
        }
        return (null);
    }

    public Integer asInt() {
        return _value;
    }
}
