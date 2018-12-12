/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.enums;

public enum SetType {
    REGULAR(0), // When the performance was not started at all yet. This is the initial status.
    WARMUP(1), // When the performance has been started but not completed. This includes performances in progress.
    DROPSET(2); // When the performance has been completed.

    private Integer _value;

    SetType(Integer val) {
        this._value = val;
    }

    public static SetType fromInt(Integer i) {
        if (i == null)
            return (null);

        for (SetType val : SetType.values()) {
            if (val.asInt() == i) {
                return (val);
            }
        }
        return (null);
    }

    public int asInt() {
        return _value;
    }
}
