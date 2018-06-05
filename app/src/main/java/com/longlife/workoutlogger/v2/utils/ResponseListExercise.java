package com.longlife.workoutlogger.v2.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.List;

public class ResponseListExercise {
    public final Status status;

    @Nullable
    public final List<Exercise> exercises;

    @Nullable
    public final Throwable error;

    private ResponseListExercise(Status status, @Nullable List<Exercise> exercises, @Nullable Throwable error) {
        this.status = status;
        this.exercises = exercises;
        this.error = error;
    }

    public static ResponseListExercise loading() {
        return (new ResponseListExercise(Status.LOADING, null, null));
    }

    public static ResponseListExercise success(@Nullable List<Exercise> exercises) {
        return (new ResponseListExercise(Status.SUCCESS, exercises, null));
    }

    public static ResponseListExercise error(@NonNull Throwable error) {
        return (new ResponseListExercise(Status.ERROR, null, error));
    }
}
