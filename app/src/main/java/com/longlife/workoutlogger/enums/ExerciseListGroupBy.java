package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.GetResource;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListGroupBy {
    public static final int GROUP_BY_NAME = 0;
    public static final int GROUP_BY_MUSCLE_GROUP_CHEST = 1;
    public static final int GROUP_BY_MUSCLE_GROUP_BACK = 2;
    public static final int GROUP_BY_MUSCLE_GROUP_ARMS = 3;
    public static final int GROUP_BY_MUSCLE_GROUP_SHOULDERS = 4;
    public static final int GROUP_BY_MUSCLE_GROUP_LEGS = 5;
    public static final int GROUP_BY_MUSCLE_GROUP_CORE = 6;

    public static List<Type> getOptions(Context context) {
        String prefix = GetResource.getStringResource(context, R.string.GROUPBY_PREFIX);

        List<Type> options = new ArrayList<>();
        options.add(new Type(GROUP_BY_NAME, GetResource.getStringResource(context, R.string.GROUPBY_NAME)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_ARMS, prefix + " " + MuscleGroup.getMuscleGroupName(context, MuscleGroup.ARMS)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_BACK, prefix + " " + MuscleGroup.getMuscleGroupName(context, MuscleGroup.BACK)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_CHEST, prefix + " " + MuscleGroup.getMuscleGroupName(context, MuscleGroup.CHEST)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_CORE, prefix + " " + MuscleGroup.getMuscleGroupName(context, MuscleGroup.CORE)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_LEGS, prefix + " " + MuscleGroup.getMuscleGroupName(context, MuscleGroup.LEGS)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_SHOULDERS, prefix + " " + MuscleGroup.getMuscleGroupName(context, MuscleGroup.SHOULDERS)));

        return options;
    }

    public static class Type {
        int id;
        String label;

        private Type(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
