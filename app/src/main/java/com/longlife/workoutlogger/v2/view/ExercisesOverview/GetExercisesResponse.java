package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.Status;

import java.util.List;

public class GetExercisesResponse {
    public final Status status;

    @Nullable
    public final List<Exercise> exercises;

    @Nullable
    public final Throwable error;

    private GetExercisesResponse(Status status, @Nullable List<Exercise> exercises, @Nullable Throwable error) {
        this.status = status;
        this.exercises = exercises;
        this.error = error;
    }

    public static GetExercisesResponse loading() {
        return (new GetExercisesResponse(Status.LOADING, null, null));
    }

    public static GetExercisesResponse success(@Nullable List<Exercise> exercises) {
        return (new GetExercisesResponse(Status.SUCCESS, exercises, null));
    }

    public static GetExercisesResponse error(@NonNull Throwable error) {
        return (new GetExercisesResponse(Status.ERROR, null, error));
    }
}
