package com.longlife.workoutlogger.v2.view.RoutineOverview;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.SessionExerciseSet;

import java.util.ArrayList;
import java.util.List;

// This is a simple object that contains the exercise and the details for associated sets.
// This is used when creating a routine.
public class RoutineExerciseHelper
{
	private Exercise exercise;
	private List<SessionExerciseSet> sets = new ArrayList<>();
	private boolean isExpanded = false;
	
	public RoutineExerciseHelper(Exercise ex, List<SessionExerciseSet> sets, boolean isExpanded)
	{
		this.exercise = ex;
		this.sets = sets;
		this.isExpanded = isExpanded;
	}
	// Getters
	public Exercise getExercise(){return exercise;}
	
	public List<SessionExerciseSet> getSets(){return sets;}
	
	public boolean IsExpanded(){return isExpanded;}
	
	public void IsExpanded(boolean bool)
	{
		isExpanded = bool;
	}
}
