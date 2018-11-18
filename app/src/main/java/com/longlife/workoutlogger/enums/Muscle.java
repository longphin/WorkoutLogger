package com.longlife.workoutlogger.enums;

public enum Muscle {
    BICEP(0),
    TRICEP(1),
    LATS(2),
    QUADS(3),
    ROTATOR_CUFF(4);

    private Integer _value;

    Muscle(Integer val) {
        this._value = val;
    }

    public static Muscle fromInt(Integer i) {
        if (i == null)
            return (null);

        for (Muscle val : Muscle.values()) {
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
