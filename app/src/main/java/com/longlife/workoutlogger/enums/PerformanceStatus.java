package com.longlife.workoutlogger.enums;

// This is the status of when a routine or exercise is being performed.
public enum PerformanceStatus {
    NEW(0), // When the performance was not started at all yet. This is the initial status.
    INCOMPLETE(1), // When the performance has been started but not completed. This includes performances in progress.
    COMPLETE(2); // When the performance has been completed.

    private Integer _value;

    PerformanceStatus(Integer val) {
        this._value = val;
    }

    public static PerformanceStatus fromInt(Integer i) {
        if (i == null)
            return (null);

        for (PerformanceStatus val : PerformanceStatus.values()) {
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
