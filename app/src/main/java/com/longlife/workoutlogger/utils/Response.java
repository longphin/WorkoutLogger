/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.utils;

import com.longlife.workoutlogger.enums.Status;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Response<T> {
    private Status status = Status.IDLE;
    private PublishSubject<Response<T>> observable = PublishSubject.create();

    @Nullable
    private T val;

    @Nullable
    private Throwable error;

    @Nullable
    public Throwable getError() {
        return error;
    }


    public void setError(@Nullable Throwable error) {
        setStatus(Status.ERROR, null, error);
    }

    // Methods
    private void setStatus(Status status, @Nullable T val, @Nullable Throwable error) {
        this.status = status;
        this.val = val;
        this.error = error;
        observable.onNext(this);
    }

    public Observable<Response<T>> getObservable() {
        return observable;
    }

    public Status getStatus() {
        return status;
    }

    public T getValue() {
        return val;
    }

    public void setSuccess(@Nullable T val) {
        setStatus(Status.SUCCESS, val, null);
    }

    public void setLoading() {
        setStatus(Status.LOADING, null, null);
    }
}

