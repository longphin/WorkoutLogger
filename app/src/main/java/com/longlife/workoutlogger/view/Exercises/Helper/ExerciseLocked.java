package com.longlife.workoutlogger.view.Exercises.Helper;

// Helper class for when an exercise is favorited.
public class ExerciseLocked
{
	private Long idExercise;
	private boolean isLocked;
	
	public ExerciseLocked(Long idExercise, boolean isLocked)
	{
		this.idExercise = idExercise;
		this.isLocked = isLocked;
	}
	
	// Getters
	public Long getIdExercise()
	{
		return idExercise;
	}
	
	public boolean isLocked()
	{
		return isLocked;
	}
}
