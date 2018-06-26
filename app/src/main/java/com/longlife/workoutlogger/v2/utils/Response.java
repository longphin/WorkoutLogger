package com.longlife.workoutlogger.v2.utils;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Response<T>
{
	private Status status = Status.IDLE;
	private PublishSubject<Response<T>> observable = PublishSubject.create();
	
	@Nullable
	private T val;
	
	@Nullable
	private Throwable error;
	
	// Getters
	public Throwable getError()
	{
		return error;
	}
	
	public Observable<Response<T>> getObservable()
	{
		return observable;
	}
	
	public Status getStatus()
	{
		return status;
	}
	
	public T getValue()
	{
		return val;
	}
	
	// Setters
	public void setError(@Nullable Throwable error)
	{
		setStatus(Status.ERROR, null, error);
	}
	
	public void setSuccess(@Nullable T val)
	{
		setStatus(Status.SUCCESS, val, null);
	}
	
	// Methods
	private void setStatus(Status status, @Nullable T val, @Nullable Throwable error)
	{
		this.status = status;
		this.val = val;
		this.error = error;
		observable.onNext(this);
	}
	
	public void setLoading()
	{
		setStatus(Status.LOADING, null, null);
	}
}
// Inner Classes
