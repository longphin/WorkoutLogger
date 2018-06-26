package com.longlife.workoutlogger.v2.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ResponseBoolean
{
	// Public
	public final Status status;
	
	@Nullable
	public final Boolean bool;
	
	@Nullable
	public final Throwable error;
	
	public ResponseBoolean(Status status, Boolean bool, Throwable error)
	{
		this.status = status;
		this.bool = bool;
		this.error = error;
	}
	
	public static ResponseBoolean loading()
	{
		return (new ResponseBoolean(Status.LOADING, null, null));
	}
	
	public static ResponseBoolean success(@Nullable Boolean bool)
	{
		return (new ResponseBoolean(Status.SUCCESS, bool, null));
	}
	
	public static ResponseBoolean error(@NonNull Throwable error)
	{
		return (new ResponseBoolean(Status.ERROR, null, error));
	}
}
// Inner Classes
