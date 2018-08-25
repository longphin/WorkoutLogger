package com.longlife.workoutlogger.view;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentHistory;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Routines.RoutinesFragment;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;
import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity
	extends ActivityBase
	implements FragNavController.RootFragmentListener,
						 FragmentBase.FragmentNavigation,
						 FragNavController.TransactionListener
{
	@Inject
	public Repository repo;
	
	// Static
	private static final String childFragmentTag = MainActivity.class.getSimpleName() + "_Fragment";
	private static final int NavigationItem_Routine = 0;
	private static final int NavigationItem_Exercise = 1;
	private static final int NavigationItem_Count = 2;
	private RoutinesViewModel routinesViewModel;
	private ExercisesViewModel exercisesViewModel;
	private AHBottomNavigation bottomNavigationBar;
	private FragNavController fragmentNavigation;
	private FragmentHistory fragmentHistory;
	private Toolbar toolbar;
	private View mView;
	
	// Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		((MyApplication)getApplication())
			.getApplicationComponent()
			.inject(this);
		
		// Get view models.
		routinesViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoutinesViewModel.class);
		exercisesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExercisesViewModel.class);
		
		// Initialize action toolbar.
		toolbar = findViewById(R.id.toolbar_main_activity);
		initToolbar();
		
		// Build bottom navigation.
		bottomNavigationBar = findViewById(R.id.bottomNav_main_activity);
		initializeBottomNavigation();
		
		// Build fragment manager to handle navigation.
		fragmentHistory = new FragmentHistory();
		buildNavigationManager(savedInstanceState);
	}
/*	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs)
	{
		if(mView == null){
			mView = super.onCreateView(name, context, attrs);
			
			toolbar = findViewById(R.id.toolbar_main_activity);
		}
		return mView;
	}*/
	
	@Override
	public void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId, String tag, String addToBackStack)
	{
		super.addFragmentToActivity(fragmentManager, fragment, frameId, tag, addToBackStack);
		
	}
	
	@Override
	public Fragment getRootFragment(int index)
	{
		switch(index){
			case NavigationItem_Routine:
				return RoutinesFragment.newInstance();
			case NavigationItem_Exercise:
				return ExercisesFragment.newInstance(R.id.frameLayout_main_activity, R.layout.fragment_exercises);
		}
		throw new IllegalStateException("Unknown fragment index.");
	}
	
	@Override
	public void onBackPressed()
	{
		if(!fragmentNavigation.isRootFragment()){
			fragmentNavigation.popFragment();
		}else{
			if(fragmentHistory.isEmpty()){
				super.onBackPressed();
			}else{
				
				
				if(fragmentHistory.getStackSize() > 1){
					
					int position = fragmentHistory.popPrevious();
					
					fragmentNavigation.switchTab(position);
					
					//updateTabSelection(position);
					
				}else{
					
					fragmentNavigation.switchTab(0);
					
					//updateTabSelection(0);
					
					fragmentHistory.emptyStack();
				}
			}
		}
	}
	// Initialize bottom navigation bar.
	
	@Override
	public void pushFragment(Fragment fragment)
	{
		if(fragmentNavigation != null){
			fragmentNavigation.pushFragment(fragment);
		}
	}
	
	@Override
	public void onTabTransaction(@Nullable Fragment fragment, int i)
	{
		if(getSupportActionBar() != null && fragmentNavigation != null){
			updateToolbar();
		}
	}
	
	@Override
	public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType)
	{
		if(getSupportActionBar() != null && fragmentNavigation != null){
			updateToolbar();
		}
	}
	
	// Methods
	private void initToolbar()
	{
		setSupportActionBar(toolbar);
	}
	
	private void buildNavigationManager(Bundle savedInstanceState)
	{
		FragNavController.Builder builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.frameLayout_main_activity);
		
		List<Fragment> fragments = new ArrayList<>(NavigationItem_Count);
		fragments.add(RoutinesFragment.newInstance());
		fragments.add(ExercisesFragment.newInstance(R.id.frameLayout_main_activity, R.layout.fragment_exercises));
		
		builder.rootFragments(fragments);
		builder.rootFragmentListener(this, NavigationItem_Count);
		
		fragmentNavigation = builder.build();
	}
	
	private void initializeBottomNavigation()
	{
		if(bottomNavigationBar == null)
			return;
		
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
				final boolean tabSwitched = openFragment(position, wasSelected);
				if(tabSwitched)
					fragmentNavigation.switchTab(position);
				return tabSwitched;
			}
		});
		// Set defaults.
		//bottomNavigationBar.setCurrentItem(NavigationItem_Routine);
		// Styles.
		bottomNavigationBar.setDefaultBackgroundColor(Color.WHITE);
		bottomNavigationBar.setAccentColor(Color.BLACK);
		bottomNavigationBar.setInactiveColor(Color.GRAY);
	}
	
	private boolean openFragment(int position, boolean wasSelected)
	{
		if(wasSelected)
			return false;
		
		// Routine fragment.
		if(position == NavigationItem_Routine){
			openRoutineFragment(wasSelected);
			return true;
		}
		// Exercise fragment.
		if(position == NavigationItem_Exercise){
			openExerciseFragment(wasSelected);
			return true;
		}
		
		throw new IllegalStateException("Tab position invalid.");
	}
	
	private void openRoutineFragment(boolean reselected)
	{
		addFragmentToActivity(manager, RoutinesFragment.newInstance(), R.id.frameLayout_main_activity, RoutinesFragment.TAG, RoutinesFragment.TAG);
		//final int thisId = NavigationItem_Routine;
		//bottomNavigationBar.setCurrentItem(thisId);
		
/*		if(reselected){
			fragmentNavigation.clearStack();
			fragmentNavigation.switchTab(thisId);
			bottomNavigationBar.setCurrentItem(thisId);
		}else{
			fragmentHistory.push(thisId);
			fragmentNavigation.switchTab(thisId);
			bottomNavigationBar.setCurrentItem(thisId);
		}*/
	}
	
	private void updateTabSelection(int currentTab)
	{
		
		for(int i = 0; i < NavigationItem_Count; i++){
			AHBottomNavigationItem selectedItem = bottomNavigationBar.getItem(i);
			if(currentTab != i){
				//selectedTab.getCustomView().setSelected(false);
			}else{
				bottomNavigationBar.setCurrentItem(i);
				//selectedTab.getCustomView().setSelected(true);
			}
		}
	}
	
	private void updateToolbar()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(!fragmentNavigation.isRootFragment());
		getSupportActionBar().setDisplayShowHomeEnabled(!fragmentNavigation.isRootFragment());
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_down_black_24dp);
	}
	
	public void openExerciseFragment(boolean reselected)
	{
		addFragmentToActivity(manager, ExercisesFragment.newInstance(R.id.frameLayout_main_activity, R.layout.fragment_exercises), R.id.frameLayout_main_activity, ExercisesFragment.TAG, ExercisesFragment.TAG);
		//final int thisId = NavigationItem_Exercise;
		//bottomNavigationBar.setCurrentItem(thisId);
		
/*		if(reselected){
			fragmentNavigation.clearStack();
			fragmentNavigation.switchTab(thisId);
			bottomNavigationBar.setCurrentItem(thisId);
		}else{
			fragmentHistory.push(thisId);
			fragmentNavigation.switchTab(thisId);
			bottomNavigationBar.setCurrentItem(thisId);
		}*/
	}
	
	public void updateToolbarTitle(String title)
	{
		
		
		getSupportActionBar().setTitle(title);
		
	}
}
// Inner Classes
