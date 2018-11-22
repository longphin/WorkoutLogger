package com.longlife.workoutlogger.enums;

import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListAdapter;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MuscleGroupWithMuscles {
    private static final int CHEST = 0;
    private static final int BACK = 1;
    private static final int ARMS = 2;
    private static final int SHOULDERS = 3;
    private static final int LEGS = 4;
    private static final int CORE = 5;

    public static List<MuscleListHelper> getAllMuscleGroups() {
        List<MuscleListHelper> groups = new ArrayList<>();
        groups.add(getMuscleGroup(ARMS));
        groups.add(getMuscleGroup(BACK));
        groups.add(getMuscleGroup(CHEST));
        groups.add(getMuscleGroup(CORE));
        groups.add(getMuscleGroup(LEGS));
        groups.add(getMuscleGroup(SHOULDERS));

        return groups;
    }

    public static MuscleListHelper getMuscleGroup(int idMuscleGroup)
    {
        List<MuscleClass> muscles = new ArrayList<>();

        Locale locale = Locale.US;
        switch (idMuscleGroup) {
            case CHEST:
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.PEC_MAJOR, MuscleClass.getName(MuscleClass.PEC_MAJOR, locale)));
                break;
            case BACK:
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.TRAPS, MuscleClass.getName(MuscleClass.TRAPS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.RHOMBOIDS, MuscleClass.getName(MuscleClass.RHOMBOIDS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.LATS, MuscleClass.getName(MuscleClass.LATS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.LOWER_BACK, MuscleClass.getName(MuscleClass.LOWER_BACK, locale)));
                break;
            case ARMS:
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.BICEPS, MuscleClass.getName(MuscleClass.BICEPS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.BICEP_BRACHIALIS, MuscleClass.getName(MuscleClass.BICEP_BRACHIALIS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.TRICEPS, MuscleClass.getName(MuscleClass.TRICEPS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.FOREARMS, MuscleClass.getName(MuscleClass.FOREARMS, locale)));
                break;
            case SHOULDERS:
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.DELTOID_ANTERIOR, MuscleClass.getName(MuscleClass.DELTOID_ANTERIOR, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.DELTOID_LATERAL, MuscleClass.getName(MuscleClass.DELTOID_LATERAL, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.DELTOID_POSTERIOR, MuscleClass.getName(MuscleClass.DELTOID_POSTERIOR, locale)));
                break;
            case LEGS:
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.QUADS, MuscleClass.getName(MuscleClass.QUADS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.HAMSTRINGS, MuscleClass.getName(MuscleClass.HAMSTRINGS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.CALVES, MuscleClass.getName(MuscleClass.CALVES, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.GLUTES, MuscleClass.getName(MuscleClass.GLUTES, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.HIP_ABDUCTORS, MuscleClass.getName(MuscleClass.HIP_ABDUCTORS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.HIP_ADDUCTORS, MuscleClass.getName(MuscleClass.HIP_ADDUCTORS, locale)));
                break;
            case CORE:
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.ABS, MuscleClass.getName(MuscleClass.ABS, locale)));
                muscles.add(new MuscleClass(idMuscleGroup, MuscleClass.OBLIQUES, MuscleClass.getName(MuscleClass.OBLIQUES, locale)));
                break;
            default:
                break;
        }

        return new MuscleListHelper(idMuscleGroup, getName(idMuscleGroup, locale), muscles, MuscleListAdapter.NUMBER_OF_COLUMNS);
    }

    private static String getName(int idMuscleGroup, Locale locale) {
        if (locale == Locale.US || locale == Locale.ENGLISH) {
            return defaultName(idMuscleGroup);
        }

        return defaultName(idMuscleGroup);
    }

    private static String defaultName(int idMuscleGroup) {
        switch (idMuscleGroup) {
            case CHEST:
                return "Chest";
            case BACK:
                return "Back";
            case ARMS:
                return "Arms";
            case SHOULDERS:
                return "Shoulders";
            case LEGS:
                return "Legs";
            case CORE:
                return "Core";
            default:
                return "Muscle group not named";
        }
    }
}
