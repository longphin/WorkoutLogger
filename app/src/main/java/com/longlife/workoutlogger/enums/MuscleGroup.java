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

    public static MuscleListHelper getMuscleGroup(Context context, int idMuscleGroup)
    {
        List<Muscle> muscles = new ArrayList<>();

        switch (idMuscleGroup) {
            case CHEST:
                muscles.add(new Muscle(idMuscleGroup, Muscle.PEC_MAJOR, GetResource.getStringResource(context, R.string.MUSCLE_pec_major)));
                break;
            case BACK:
                muscles.add(new Muscle(idMuscleGroup, Muscle.TRAPS, GetResource.getStringResource(context, R.string.MUSCLE_traps)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.RHOMBOIDS, GetResource.getStringResource(context, R.string.MUSCLE_rhomboids)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.LATS, GetResource.getStringResource(context, R.string.MUSCLE_lats)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.LOWER_BACK, GetResource.getStringResource(context, R.string.MUSCLE_lower_back)));
                break;
            case ARMS:
                muscles.add(new Muscle(idMuscleGroup, Muscle.BICEPS, GetResource.getStringResource(context, R.string.MUSCLE_biceps)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.BICEP_BRACHIALIS, GetResource.getStringResource(context, R.string.MUSCLE_bicep_brachialis)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.TRICEPS, GetResource.getStringResource(context, R.string.MUSCLE_triceps)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.FOREARMS, GetResource.getStringResource(context, R.string.MUSCLE_forearms)));
                break;
            case SHOULDERS:
                muscles.add(new Muscle(idMuscleGroup, Muscle.DELTOID_ANTERIOR, GetResource.getStringResource(context, R.string.MUSCLE_deltoid_anterior)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.DELTOID_LATERAL, GetResource.getStringResource(context, R.string.MUSCLE_deltoid_lateral)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.DELTOID_POSTERIOR, GetResource.getStringResource(context, R.string.MUSCLE_deltoid_posterior)));
                break;
            case LEGS:
                muscles.add(new Muscle(idMuscleGroup, Muscle.QUADS, GetResource.getStringResource(context, R.string.MUSCLE_quads)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.HAMSTRINGS, GetResource.getStringResource(context, R.string.MUSCLE_hamstrings)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.CALVES, GetResource.getStringResource(context, R.string.MUSCLE_calves)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.GLUTES, GetResource.getStringResource(context, R.string.MUSCLE_glutes)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.HIP_ABDUCTORS, GetResource.getStringResource(context, R.string.MUSCLE_hip_abductors)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.HIP_ADDUCTORS, GetResource.getStringResource(context, R.string.MUSCLE_hip_adductors)));
                break;
            case CORE:
                muscles.add(new Muscle(idMuscleGroup, Muscle.ABS, GetResource.getStringResource(context, R.string.MUSCLE_abs)));
                muscles.add(new Muscle(idMuscleGroup, Muscle.OBLIQUES, GetResource.getStringResource(context, R.string.MUSCLE_obliques)));
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
