package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.view.Exercises.ExercisesActivity;
import com.longlife.workoutlogger.view.Routines.RoutinesActivity;

import javax.inject.Inject;

public class MainActivity
	extends AppCompatActivity
{
	@Inject
	public Repository repo;
	
	// Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	// Called when the user presses the Routines button.
	public void gotoRoutines(View view)
	{
		//Intent intent = new Intent(this, RoutinesActivity.class);
		Intent intent = new Intent(this, RoutinesActivity.class);
		startActivity(intent);
	}
	
	// Called when user presses the History button.
	public void gotoHistory(View view)
	{
		// [TODO]
	}
	
	// Called when user presses the Goals button.
	public void gotoExercises(View view)
	{
		Intent intent = new Intent(this, ExercisesActivity.class);
		startActivity(intent);
	}
}
// Inner Classes
