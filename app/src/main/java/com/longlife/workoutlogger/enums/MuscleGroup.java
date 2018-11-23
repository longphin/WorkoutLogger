package com.longlife.workoutlogger.enums;

import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListAdapter;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MuscleGroup {
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
        List<Muscle> muscles = new ArrayList<>();

        Locale locale = Locale.US;
        switch (idMuscleGroup) {
            case CHEST:
                muscles.add(new Muscle(idMuscleGroup, Muscle.PEC_MAJOR, Muscle.getName(Muscle.PEC_MAJOR, locale)));
                break;
            case BACK:
                muscles.add(new Muscle(idMuscleGroup, Muscle.TRAPS, Muscle.getName(Muscle.TRAPS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.RHOMBOIDS, Muscle.getName(Muscle.RHOMBOIDS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.LATS, Muscle.getName(Muscle.LATS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.LOWER_BACK, Muscle.getName(Muscle.LOWER_BACK, locale)));
                break;
            case ARMS:
                muscles.add(new Muscle(idMuscleGroup, Muscle.BICEPS, Muscle.getName(Muscle.BICEPS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.BICEP_BRACHIALIS, Muscle.getName(Muscle.BICEP_BRACHIALIS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.TRICEPS, Muscle.getName(Muscle.TRICEPS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.FOREARMS, Muscle.getName(Muscle.FOREARMS, locale)));
                break;
            case SHOULDERS:
                muscles.add(new Muscle(idMuscleGroup, Muscle.DELTOID_ANTERIOR, Muscle.getName(Muscle.DELTOID_ANTERIOR, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.DELTOID_LATERAL, Muscle.getName(Muscle.DELTOID_LATERAL, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.DELTOID_POSTERIOR, Muscle.getName(Muscle.DELTOID_POSTERIOR, locale)));
                break;
            case LEGS:
                muscles.add(new Muscle(idMuscleGroup, Muscle.QUADS, Muscle.getName(Muscle.QUADS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.HAMSTRINGS, Muscle.getName(Muscle.HAMSTRINGS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.CALVES, Muscle.getName(Muscle.CALVES, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.GLUTES, Muscle.getName(Muscle.GLUTES, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.HIP_ABDUCTORS, Muscle.getName(Muscle.HIP_ABDUCTORS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.HIP_ADDUCTORS, Muscle.getName(Muscle.HIP_ADDUCTORS, locale)));
                break;
            case CORE:
                muscles.add(new Muscle(idMuscleGroup, Muscle.ABS, Muscle.getName(Muscle.ABS, locale)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.OBLIQUES, Muscle.getName(Muscle.OBLIQUES, locale)));
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
