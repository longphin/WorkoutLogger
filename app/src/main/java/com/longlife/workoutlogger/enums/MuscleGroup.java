package com.longlife.workoutlogger.enums;

import java.util.ArrayList;
import java.util.List;

public enum MuscleGroup {
    ARMS(0),
    LEGS(1),
    ABS(2),
    BACK(3);

    private Integer _value;

    MuscleGroup(Integer val) {
        this._value = val;
    }

    public static MuscleGroup fromInt(Integer i) {
        if (i == null)
            return (null);

        for (MuscleGroup val : MuscleGroup.values()) {
            if (val.asInt().equals(i)) {
                return (val);
            }
        }
        return (null);
    }

    public Integer asInt() {
        return _value;
    }

    public static List<Muscle> getRelatedMuscles(MuscleGroup group) {
        List<Muscle> muscles = new ArrayList<>();
        if (group == ARMS) {
            muscles.add(Muscle.BICEP);
            muscles.add(Muscle.TRICEP);
        }

        if (group == LEGS) {
            muscles.add(Muscle.QUADS);
        }

        if (group == ABS) {

        }

        if (group == BACK) {
            muscles.add(Muscle.LATS);
        }

        return muscles;
    }
}
