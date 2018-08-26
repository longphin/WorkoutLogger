package com.longlife.workoutlogger.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentHistory;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Routines.RoutinesFragment;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;
import com.ncapdevi.fragnav.FragNavController;

import javax.inject.Inject;

public class MainActivity
	extends ActivityBase
	implements FragNavController.RootFragmentListener,
						 FragmentBase.FragmentNavigation,
						 FragNavController.TransactionListener
{
	@Inject
	public Repository repo;
	private ExercisesViewModel exercisesViewModel;
	private RoutinesViewModel routinesViewModel;
	private AHBottomNavigation bottomTabLayout;
	private FragNavController mNavController;
	// Private
	private final AHBottomNavigation.OnTabSelectedListener onTabSelectedListener =
		new AHBottomNavigation.OnTabSelectedListener()
		{
			// Overrides
			@Override
			public boolean onTabSelected(int position, boolean wasSelected)
			{
				if(wasSelected) // Item was reselected.
				{
					mNavController.clearStack();
					//switchTab(position);
					return true;
				}else{ // Switching item.
					fragmentHistory.push(position);
					switchTab(position);
					updateTabSelection(position);
					return true;
				}
			}
		};
	
	//TabLayout bottomTabLayout;
	// Other
	FrameLayout contentFrame;
	Toolbar toolbar;
	
	private FragmentHistory fragmentHistory;
	String[] TABS = {"Routine", "Exercise"};
	
	// Overrides
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		contentFrame = findViewById(R.id.frameLayout_main_activity);
		toolbar = findViewById(R.id.toolbar_main_activity);
		bottomTabLayout = findViewById(R.id.bottomNav_main_activity);
		
		initToolbar();
		
		initTab();
		
		fragmentHistory = new FragmentHistory();
		
		mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.frameLayout_main_activity)
			.transactionListener(this)
			.rootFragmentListener(this, TABS.length)
			.build();
		
		switchTab(0);
		
		setOnTabSelectedListener();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onStop()
	{
		
		super.onStop();
	}
	
	/*private void initTab() {
		if (bottomTabLayout != null) {
			for (int i = 0; i < TABS.length; i++) {
				bottomTabLayout.addTab(bottomTabLayout.newTab());
				TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
				if (tab != null)
					tab.setCustomView(getTabView(i));
			}
		}
	}
	
	
	private View getTabView(int position) {
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_bottom, null);
		ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
		icon.setImageDrawable(Utils.setDrawableSelector(MainActivity.this, mTabIconsSelected[position], mTabIconsSelected[position]));
		return view;
	}*/
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onBackPressed()
	{
		
		if(!mNavController.isRootFragment()){
			mNavController.popFragment();
		}else{
			
			if(fragmentHistory.isEmpty()){
				super.onBackPressed();
			}else{
				
				if(fragmentHistory.getStackSize() > 1){
					
					int position = fragmentHistory.popPrevious();
					
					switchTab(position);
					
					updateTabSelection(position);
					
				}else{
					/*// When backtracking is finished, exit app.
					super.onBackPressed();
					*/
					
					// Alternatively, we may want to go to the Home tab
					switchTab(0);

					updateTabSelection(0);
					
					fragmentHistory.emptyStack();
				}
			}
			
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		if(mNavController != null){
			mNavController.onSaveInstanceState(outState);
		}
	}
	
	@Override
	public void pushFragment(Fragment fragment)
	{
		if(mNavController != null){
			mNavController.pushFragment(fragment);
		}
	}
	
	
/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		
		
		return super.onOptionsItemSelected(item);
		
	}*/
	
	@Override
	public void onTabTransaction(Fragment fragment, int index)
	{
		// If we have a backstack, show the back button
		if(getSupportActionBar() != null && mNavController != null){
			updateToolbar();
		}
	}
	
	@Override
	public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType)
	{
		//do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
		// If we have a backstack, show the back button
		if(getSupportActionBar() != null && mNavController != null){
			updateToolbar();
		}
	}
	
	@Override
	public Fragment getRootFragment(int index)
	{
		switch(index){
			
			case FragNavController.TAB1:
				return RoutinesFragment.newInstance();
			case FragNavController.TAB2:
				return ExercisesFragment.newInstance(R.id.frameLayout_main_activity, R.layout.fragment_exercises);
		}
		throw new IllegalStateException("Need to send an index that we know");
	}
	
	// Methods
	private void setOnTabSelectedListener()
	{
		/*bottomTabLayout.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener(){
			@Override
			public boolean onTabSelected(int position, boolean wasSelected)
			{
				if(wasSelected) // Item was reselected.
				{
					mNavController.clearStack();
					switchTab(position);
					return true;
				}else{ // Switching item.
					fragmentHistory.push(position);
					switchTab(position);
					return true;
				}
			}
		});*/
		bottomTabLayout.setOnTabSelectedListener(onTabSelectedListener);
	}
	
	private void initToolbar()
	{
		setSupportActionBar(toolbar);
		
		
	}
	
	private void switchTab(int position)
	{
		mNavController.switchTab(position);
		bottomTabLayout.enableItemAtPosition(position);
		//updateTabSelection(position);
	}
	
	private void updateTabSelection(int currentTab)
	{
	
/*		for (int i = 0; i <  TABS.length; i++) {
			TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
			if(currentTab != i) {
				selectedTab.getCustomView().setSelected(false);
			}else{
				selectedTab.getCustomView().setSelected(true);
			}
		}*/
		bottomTabLayout.removeOnTabSelectedListener();
		bottomTabLayout.setCurrentItem(currentTab);
		setOnTabSelectedListener();
		//bottomTabLayout.enableItemAtPosition(currentTab);
	}
	
	private void updateToolbar()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
		getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_down_black_24dp);
	}
	
	private void initTab()
	{
		if(bottomTabLayout == null)
			return;
		
		// Settings
		bottomTabLayout.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW); // Always show the labels for items.
		// Initialize items.
		AHBottomNavigationItem RoutineItem = new AHBottomNavigationItem(getString(R.string.NavBar_Routines), R.drawable.ic_settings_ethernet_black_24dp);
		AHBottomNavigationItem ExerciseItem = new AHBottomNavigationItem(getString(R.string.NavBar_Exercises), R.drawable.ic_settings_ethernet_black_24dp);
		// Add navigation items.
		bottomTabLayout.addItem(RoutineItem);
		bottomTabLayout.addItem(ExerciseItem);
		// Styles.
		bottomTabLayout.setDefaultBackgroundColor(Color.WHITE);
		bottomTabLayout.setAccentColor(Color.BLACK);
		bottomTabLayout.setInactiveColor(Color.GRAY);
	}
	
	
	//    private void updateToolbarTitle(int position){
	//
	//
	//        getSupportActionBar().setTitle(TABS[position]);
	//
	//    }
	
	public void updateToolbarTitle(String title)
	{
		getSupportActionBar().setTitle(title);
	}
}
// Inner Classes
