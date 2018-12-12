package com.longlife.workoutlogger.enums;

import android.content.Context;
import android.util.Log;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.MuscleGroupEntity;
import com.longlife.workoutlogger.utils.GetResource;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListAdapter;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListHelper;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroup {
    public static final int CHEST = 0;
    public static final int BACK = 1;
    public static final int ARMS = 2;
    public static final int SHOULDERS = 3;
    public static final int LEGS = 4;
    public static final int CORE = 5;

    private static final String TAG = MuscleGroup.class.getSimpleName();

    public static List<MuscleListHelper> getAllMuscleGroups(Context context) {
        List<MuscleListHelper> groups = new ArrayList<>();
        for (Integer idMuscleGroup : MuscleGroup.getAllMuscleGroupsIds()) {
            groups.add(getMuscleGroup(context, idMuscleGroup));
        }

        return groups;
    }

    public static List<Integer> getAllMuscleGroupsIds() {
        List<Integer> idMuscleGroups = new ArrayList<>();
        idMuscleGroups.add(ARMS);
        idMuscleGroups.add(BACK);
        idMuscleGroups.add(CHEST);
        idMuscleGroups.add(CORE);
        idMuscleGroups.add(LEGS);
        idMuscleGroups.add(SHOULDERS);

        return idMuscleGroups;
    }

    // Return the MuscleGroup id given an idMuscle. Used to determine what group a muscle is a part of.
    public static int getMuscleGroupForMuscle(int idMuscle) {
        switch (idMuscle) {
            case Muscle.UPPER_PEC:
            case Muscle.MIDDLE_PEC:
            case Muscle.LOWER_PEC:
                return CHEST;

            case Muscle.TRAPS:
            case Muscle.RHOMBOIDS:
            case Muscle.LATS:
            case Muscle.LOWER_BACK:
                return BACK;

            case Muscle.BICEPS:
            case Muscle.BICEP_BRACHIALIS:
            case Muscle.TRICEPS:
            case Muscle.FOREARMS:
                return ARMS;

            case Muscle.DELTOID_ANTERIOR:
            case Muscle.DELTOID_LATERAL:
            case Muscle.DELTOID_POSTERIOR:
                return SHOULDERS;

            case Muscle.QUADS:
            case Muscle.HAMSTRINGS:
            case Muscle.CALVES:
            case Muscle.GLUTES:
            case Muscle.HIP_ABDUCTORS:
            case Muscle.HIP_ADDUCTORS:
                return LEGS;

            case Muscle.ABS:
            case Muscle.OBLIQUES:
            case Muscle.SERRATUS:
                return CORE;
        }

        Log.d(TAG, "Could not find muscle group by " + String.valueOf(idMuscle));
        return -1;
    }

    private static MuscleListHelper getMuscleGroup(Context context, int idMuscleGroup)
    {
        List<Muscle> muscles = new ArrayList<>();

        for (Integer idMuscle : Muscle.getAllMuscles())//getAllMuscleGroupsIds())
        {
            if (getMuscleGroupForMuscle(idMuscle) == idMuscleGroup) {
                muscles.add(new Muscle(context, idMuscleGroup, idMuscle));
            }
        }

        /*
        switch (idMuscleGroup) {
            case CHEST:
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.UPPER_PEC));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.MIDDLE_PEC));
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.LOWER_PEC));
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
                muscles.add(new Muscle(context, idMuscleGroup, Muscle.SERRATUS));
                break;
        }
        */

        return new MuscleListHelper(idMuscleGroup, getMuscleGroupName(context, idMuscleGroup), muscles, MuscleListAdapter.NUMBER_OF_COLUMNS);
    }

    public static String getMuscleGroupName(Context context, int idMuscleGroup) {
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

    public static List<MuscleGroupEntity> getAllMuscleGroupEntities() {
        List<MuscleGroupEntity> muscleGroups = new ArrayList<>();
        for (Integer idMuscleGroup : MuscleGroup.getAllMuscleGroupsIds()) {
            muscleGroups.add(new MuscleGroupEntity(idMuscleGroup));
        }

        return muscleGroups;
    }
}
