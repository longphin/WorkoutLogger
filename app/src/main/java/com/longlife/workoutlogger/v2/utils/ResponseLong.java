package com.longlife.workoutlogger.v2.utils;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ResponseLong {
    public Status status;
    private PublishSubject<ResponseLong> observable = PublishSubject.create();
    @Nullable
    private Long id;

    @Nullable
    private Throwable error;

    // Getters
    public Long getValue() {
        return id;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(@Nullable Throwable error) {
        setStatus(Status.ERROR, null, error);
    }

    public Observable<ResponseLong> getObservable() {
        return observable;
    }

    // Setters
    private void setStatus(Status status, @Nullable Long id, @Nullable Throwable error) {
        this.status = status;
        this.id = id;
        this.error = error;
        observable.onNext(this);
    }

    public void setSuccess(@Nullable Long id) {
        setStatus(Status.SUCCESS, id, null);
    }

    public void setLoading() {
        setStatus(Status.LOADING, null, null);
    }
}
