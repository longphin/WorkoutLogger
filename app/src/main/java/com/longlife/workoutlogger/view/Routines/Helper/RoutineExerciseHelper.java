package com.longlife.workoutlogger.view.Routines.Helper;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.List;

// This is a simple object that contains the exercise and the details for associated sets.
// This is used when creating a routine.
public class RoutineExerciseHelper
{
	// Getters
	
	private Exercise exercise;
	private List<SessionExerciseSet> sets;
	private boolean isExpanded;
	
	public RoutineExerciseHelper(Exercise ex, List<SessionExerciseSet> sets, boolean isExpanded)
	{
		this.exercise = ex;
		this.sets = sets;
		this.isExpanded = isExpanded;
	}
	
	// Setters
	public void setExercise(Exercise exercise)
	{
		this.exercise = exercise;
	}
	public Exercise getExercise(){return exercise;}
	
	public List<SessionExerciseSet> getSets(){return sets;}
	
	public boolean IsExpanded(){return isExpanded;}
	
	public void IsExpanded(boolean bool)
	{
		isExpanded = bool;
	}
}
