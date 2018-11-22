package com.longlife.workoutlogger.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum MuscleGroup {
    ARMS(0),
    LEGS(1),
    ABS(2),
    BACK(3);

    private Integer _value;

    private static Map<MuscleGroup, List<Muscle>> muscleGroups = createMap();

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

    public static Map<MuscleGroup, List<Muscle>> getMuscleGroups() {
        return muscleGroups;
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

    private static Map<MuscleGroup, List<Muscle>> createMap() {
        Map<MuscleGroup, List<Muscle>> map = new HashMap<>();
        map.put(ARMS, Arrays.asList(Muscle.BICEP, Muscle.TRICEP, Muscle.ROTATOR_CUFF));

        map.put(LEGS, Arrays.asList(Muscle.QUADS));

        return map;
    }

    public String getName(MuscleGroup group) {
        switch (group) {
            case ARMS:
                return "Arms";
            case LEGS:
                return "Legs";
            case ABS:
                return "Abs";
            case BACK:
                return "Back";
            default:
                return "error - unknown group";
        }
    }
}
