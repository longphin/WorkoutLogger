package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.longlife.workoutlogger.v2.utils.Status;

public class InsertExerciseResponse {
    public final Status status;

    @Nullable
    public final Long id;

    @Nullable
    public final Throwable error;

    private InsertExerciseResponse(Status status, @Nullable Long id, @Nullable Throwable error) {
        this.status = status;
        this.id = id;
        this.error = error;
    }

    public static InsertExerciseResponse loading() {
        return (new InsertExerciseResponse(Status.LOADING, null, null));
    }

    public static InsertExerciseResponse success(@Nullable Long id) {
        return (new InsertExerciseResponse(Status.SUCCESS, id, null));
    }

    public static InsertExerciseResponse error(@NonNull Throwable error) {
        return (new InsertExerciseResponse(Status.ERROR, null, error));
    }
}
