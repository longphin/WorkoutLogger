package com.longlife.workoutlogger.enums;

public enum Status
{
	IDLE, // only occurs the first time a Response is initialized. It will have no status, so it is set as idle.
	LOADING,
	SUCCESS,
	ERROR
}