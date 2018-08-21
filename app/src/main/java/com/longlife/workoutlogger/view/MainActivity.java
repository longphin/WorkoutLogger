package com.longlife.workoutlogger.view;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Routines.RoutinesFragment;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import javax.inject.Inject;

public class MainActivity
	extends ActivityBase
{
	@Inject
	public Repository repo;
	
	// Static
	private static final String childFragmentTag = MainActivity.class.getSimpleName() + "_Fragment";
	private static int NavigationItem_Routine = 0;
	private static int NavigationItem_Exercise = 1;
	private RoutinesViewModel routinesViewModel;
	private ExercisesViewModel exercisesViewModel;
	private AHBottomNavigation bottomNavigationBar;

	// Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		((MyApplication)getApplication())
			.getApplicationComponent()
			.inject(this);
		
		routinesViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoutinesViewModel.class);
		
		exercisesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExercisesViewModel.class);
		
		bottomNavigationBar = findViewById(R.id.bottomNav_main_activity);
		initializeBottomNavigation();
	}
	
	// Methods
	// Initialize bottom navigation bar.
	private void initializeBottomNavigation()
	{
		// Settings
		bottomNavigationBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW); // Always show the labels for items.
		// Initialize items.
		AHBottomNavigationItem RoutineItem = new AHBottomNavigationItem(getString(R.string.NavBar_Routines), R.drawable.ic_settings_ethernet_black_24dp);
		AHBottomNavigationItem ExerciseItem = new AHBottomNavigationItem(getString(R.string.NavBar_Exercises), R.drawable.ic_settings_ethernet_black_24dp);
		// Add navigation items.
		bottomNavigationBar.addItem(RoutineItem);
		bottomNavigationBar.addItem(ExerciseItem);
		// Add listeners when selecting the items.
		bottomNavigationBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener()
		{
			// Overrides
			@Override
			public boolean onTabSelected(int position, boolean wasSelected)
			{
				// If the same item was selected, do nothing.
				//if(wasSelected) return true; // [TODO] If the current fragment is one of the base ones (RoutinesFragment, ExercisesFragment), then do nothing. If not, then go to the base fragment.
				
				// Routine fragment.
				if(position == NavigationItem_Routine){
					openRoutineFragment();
					return true;
				}
				// Exercise fragment.
				if(position == NavigationItem_Exercise){
					openExerciseFragment();
					return true;
				}
				
				// Touch not handled.
				return false;
			}
		});
		// Set defaults.
		bottomNavigationBar.setCurrentItem(NavigationItem_Routine);
		// Styles.
		bottomNavigationBar.setDefaultBackgroundColor(Color.WHITE);
		bottomNavigationBar.setAccentColor(Color.BLACK);
		bottomNavigationBar.setInactiveColor(Color.GRAY);
	}
	
	private void openRoutineFragment()
	{
		addFragmentToActivity(manager, RoutinesFragment.newInstance(), R.id.frameLayout_main_activity, childFragmentTag);
	}
	
	public void openExerciseFragment()
	{
		addFragmentToActivity(manager, ExercisesFragment.newInstance(exercisesViewModel, R.id.frameLayout_main_activity, R.layout.fragment_exercises), R.id.frameLayout_main_activity, childFragmentTag);
	}
}
// Inner Classes
