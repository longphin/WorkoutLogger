package com.longlife.workoutlogger.view.Exercises.Helper;

// Helper class for when an exercise is favorited.
public class ExerciseFavorited
{
	private Long idExercise;
	private boolean isFavorited;
	
	public ExerciseFavorited(Long idExercise, boolean isFavorited)
	{
		this.idExercise = idExercise;
		this.isFavorited = isFavorited;
	}
	
	// Getters
	public Long getIdExercise()
	{
		return idExercise;
	}
	
	public boolean isFavorited()
	{
		return isFavorited;
	}
}
