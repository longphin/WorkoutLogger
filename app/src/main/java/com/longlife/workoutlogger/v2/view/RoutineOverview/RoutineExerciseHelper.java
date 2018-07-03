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
	
	public RoutineExerciseHelper(Exercise ex)
	{
		exercise = ex;
	}
	
	// Getters
	public Exercise getExercise(){return exercise;}
	
	public List<SessionExerciseSet> getSets(){return sets;}
}
