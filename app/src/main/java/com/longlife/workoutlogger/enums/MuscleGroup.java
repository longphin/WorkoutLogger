package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.GetResource;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListAdapter;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListHelper;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroup {
    private static final int CHEST = 0;
    private static final int BACK = 1;
    private static final int ARMS = 2;
    private static final int SHOULDERS = 3;
    private static final int LEGS = 4;
    private static final int CORE = 5;

    public static List<MuscleListHelper> getAllMuscleGroups(Context context) {
        List<MuscleListHelper> groups = new ArrayList<>();
        groups.add(getMuscleGroup(context, ARMS));
        groups.add(getMuscleGroup(context, BACK));
        groups.add(getMuscleGroup(context, CHEST));
        groups.add(getMuscleGroup(context, CORE));
        groups.add(getMuscleGroup(context, LEGS));
        groups.add(getMuscleGroup(context, SHOULDERS));

        return groups;
    }

    private static MuscleListHelper getMuscleGroup(Context context, int idMuscleGroup)
    {
        List<Muscle> muscles = new ArrayList<>();

        switch (idMuscleGroup) {
            case CHEST:
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.PEC_MAJOR));
                break;
            case BACK:
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.TRAPS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.RHOMBOIDS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.LATS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.LOWER_BACK));
                break;
            case ARMS:
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.BICEPS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.BICEP_BRACHIALIS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.TRICEPS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.FOREARMS));
                break;
            case SHOULDERS:
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.DELTOID_ANTERIOR));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.DELTOID_LATERAL));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.DELTOID_POSTERIOR));
                break;
            case LEGS:
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.QUADS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.HAMSTRINGS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.CALVES));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.GLUTES));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.HIP_ABDUCTORS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.HIP_ADDUCTORS));
                break;
            case CORE:
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.ABS));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.OBLIQUES));
                break;
            default:
                break;
        }

        return new MuscleListHelper(idMuscleGroup, getMuscleGroupName(context, idMuscleGroup), muscles, MuscleListAdapter.NUMBER_OF_COLUMNS);
    }

    private static String getMuscleGroupName(Context context, int idMuscleGroup) {
        switch (idMuscleGroup) {
            case CHEST:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_chest);
            case BACK:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_back);
            case ARMS:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_arms);
            case SHOULDERS:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_shoulders);
            case LEGS:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_legs);
            case CORE:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_core);
            default:
                return "Muscle group not named";
        }
    }
}
