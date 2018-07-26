package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import com.longlife.workoutlogger.v2.model.Exercise;

// This is a helper class for when an exercise is being inserted.
// [TODO] this isn't needed.
public class ExerciseInsertHelper
{
	// The exercise being inserted.
	private Exercise exercise;
	// The position of where the exercise is inserted, after reordering.
	private int pos;
	
	public ExerciseInsertHelper(Exercise exercise, int pos)
	{
		this.exercise = exercise;
		this.pos = pos;
	}
	
	// Getters
	public Exercise getExercise(){return exercise;}
	
	public int getInsertPosition(){return pos;}
	
	// Setters
	public void setInsertPosition(int insertPosition)
	{
		this.pos = insertPosition;
	}
}
