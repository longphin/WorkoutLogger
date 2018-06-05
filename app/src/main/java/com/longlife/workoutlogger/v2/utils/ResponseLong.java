package com.longlife.workoutlogger.v2.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ResponseLong {
    public final Status status;

    @Nullable
    public final Long id;

    @Nullable
    public final Throwable error;

    private ResponseLong(Status status, @Nullable Long id, @Nullable Throwable error) {
        this.status = status;
        this.id = id;
        this.error = error;
    }

    public static ResponseLong loading() {
        return (new ResponseLong(Status.LOADING, null, null));
    }

    public static ResponseLong success(@Nullable Long id) {
        return (new ResponseLong(Status.SUCCESS, id, null));
    }

    public static ResponseLong error(@NonNull Throwable error) {
        return (new ResponseLong(Status.ERROR, null, error));
    }
}
