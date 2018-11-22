package com.longlife.workoutlogger.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MuscleGroupWithMuscles {
    private static final int ARMS = 0;
    private static final int LEGS = 1;
    private static final int CORE = 2;
    private static final int BACK = 3;

    /*
    private static final Map<Integer, List<Muscle>> musclesGroups = createMap();

    private static Map<Integer, List<Muscle>> createMap()
    {
        Map<Integer, List<Muscle>> map = new HashMap<>();
        map.put(ARMS, Arrays.asList(Muscle.BICEP, Muscle.TRICEP, Muscle.ROTATOR_CUFF));

        map.put(LEGS, Arrays.asList(Muscle.QUADS));

        return map;
    }
    */

    public static List<MuscleClass> getMusclesWithGroupHeaders() {
        List<MuscleClass> res = new ArrayList<>();
        Locale locale = Locale.US;
        res.add(new MuscleClass(ARMS, getName(ARMS, locale)));
        res.add(new MuscleClass(ARMS, MuscleClass.BICEP, MuscleClass.getName(MuscleClass.BICEP, locale)));
        res.add(new MuscleClass(ARMS, MuscleClass.TRICEP, MuscleClass.getName(MuscleClass.TRICEP, locale)));

        res.add(new MuscleClass(LEGS, getName(LEGS, locale)));
        res.add(new MuscleClass(LEGS, MuscleClass.QUADS, MuscleClass.getName(MuscleClass.QUADS, locale)));

        return res;
    }

    private static String getName(int idMuscleGroup, Locale locale) {
        if (locale == Locale.US || locale == Locale.ENGLISH) {
            return defaultName(idMuscleGroup);
        }

        return defaultName(idMuscleGroup);
    }

    private static String defaultName(int idMuscleGroup) {
        switch (idMuscleGroup) {
            case ARMS:
                return "Arms";
            case LEGS:
                return "Legs";
            case CORE:
                return "Core";
            case BACK:
                return "Back";
            default:
                return "Muscle group not named";
        }
    }
}
