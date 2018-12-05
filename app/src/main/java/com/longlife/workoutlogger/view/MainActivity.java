package com.longlife.workoutlogger.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentHistory;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.TimerNotificationService;
import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.Profile;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesListFragment;
import com.longlife.workoutlogger.view.Profile.ProfileFragment;
import com.longlife.workoutlogger.view.Profile.ProfileViewModel;
import com.longlife.workoutlogger.view.Routines.RoutinesFragment;
import com.ncapdevi.fragnav.FragNavController;

import javax.inject.Inject;

import io.reactivex.observers.DisposableMaybeObserver;

public class MainActivity
        extends ActivityBase
        implements FragNavController.RootFragmentListener,
        FragmentBase.FragmentNavigation,
        FragNavController.TransactionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    public Repository repo;

    // Broadcast receiver for rest timer notification.
    BroadcastReceiver restTimerReceiver;

    private ProfileViewModel profileViewModel;
    private AHBottomNavigation bottomTabLayout;
    private FragNavController mNavController;
    private FragmentHistory fragmentHistory;
    private boolean enableBottomNavClick = true;
    private FrameLayout contentFrame;
    private Toolbar toolbar;
    private String[] TABS = {"Profile", "Routine", "Exercise", "Perform"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) this.getApplication())
                .getApplicationComponent()
                .inject(this);

        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);
        getProfile();

        // Get UI.
        contentFrame = findViewById(R.id.frameLayout_main_activity);
        bottomTabLayout = findViewById(R.id.bottomNav_main_activity);

        initToolbar();

        initBottomNavigation();

        fragmentHistory = new FragmentHistory();

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.frameLayout_main_activity)
                .transactionListener(this)
                .rootFragmentListener(this, TABS.length)
                .build();

        switchTab(0);

        setOnTabSelectedListener();

        restTimerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final int headerIndex = intent.getIntExtra(TimerNotificationService.EXTRA_HEADERINDEX, -1);
                final int setIndex = intent.getIntExtra(TimerNotificationService.EXTRA_SETINDEX, -1);

                Log.d(TAG, "Timer finished for " + String.valueOf(headerIndex) + " - " + String.valueOf(setIndex));
                ((MyApplication) getApplication()).stopTimerNotificationService();
                // [TODO] Send the result to the performing fragment.
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    // Initialize user profile. If one does not exist in database, then one will be inserted.
    private void getProfile() {
        // Do back end stuff on launch.
        profileViewModel.getProfile()
                .subscribe(new DisposableMaybeObserver<Profile>() {

                    @Override
                    public void onSuccess(Profile profile) // Successfully loaded a profile.
                    {
                        processProfile(profile);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Could not load profile.");
                    }

                    @Override
                    public void onComplete() // If there is no profile, then we need to insert a record.
                    {
                        addDisposable(profileViewModel.insertProfile(new Profile()).subscribe(profile -> processProfile(profile)));
                    }
                });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view ->
        {
            onBackPressed();
        });
    }

    private void initBottomNavigation() {
        if (bottomTabLayout == null)
            return;

        // Settings
        bottomTabLayout.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW); // Always show the labels for items.
        // Initialize items.
        AHBottomNavigationItem ProfileItem = new AHBottomNavigationItem(getString(R.string.NavBar_Profile), R.drawable.ic_person_black_24dp);
        AHBottomNavigationItem RoutineItem = new AHBottomNavigationItem(getString(R.string.NavBar_Routines), R.drawable.ic_storage_black_24dp);
        AHBottomNavigationItem ExerciseItem = new AHBottomNavigationItem(getString(R.string.NavBar_Exercises), R.drawable.ic_weightlifting);
        AHBottomNavigationItem PerformItem = new AHBottomNavigationItem(getString(R.string.NavBar_Perform), R.drawable.ic_play_arrow_black_24dp);
        // Add navigation items.
        bottomTabLayout.addItem(ProfileItem);
        bottomTabLayout.addItem(RoutineItem);
        bottomTabLayout.addItem(ExerciseItem);
        bottomTabLayout.addItem(PerformItem);
        // Styles.
        bottomTabLayout.setDefaultBackgroundColor(Color.WHITE);
        bottomTabLayout.setAccentColor(Color.BLACK);
        bottomTabLayout.setInactiveColor(Color.GRAY);
    }

    private void switchTab(int position) {
        mNavController.switchTab(position);
        bottomTabLayout.enableItemAtPosition(position);
        //updateTabSelection(position);
    }

    private void setOnTabSelectedListener() {
        bottomTabLayout.setOnTabSelectedListener(
                new AHBottomNavigation.OnTabSelectedListener() {

                    @Override
                    public boolean onTabSelected(int position, boolean wasSelected) {
                        if (!enableBottomNavClick)
                            return true;
                        if (wasSelected) // Item was reselected.
                        {
                            //mNavController.clearStack();
                            //switchTab(position);
                            //return true;
                            return true;
                        } else { // Switching item.
                            fragmentHistory.push(position);
                            switchTab(position);
                            updateTabSelection(position);
                            return true;
                        }
                    }
                }
        );
    }

    // Methods
    private void processProfile(@NonNull Profile profile) {
        profileViewModel.setCachedProfile(profile);
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
            hideKeyboard(this);
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {

                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    switchTab(position);

                    updateTabSelection(position);

                    hideKeyboard(this);
                } else {
					/*// When backtracking is finished, exit app.
					super.onBackPressed();
					*/

                    // Alternatively, we may want to go to the Home tab
                    switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();

                    hideKeyboard(this);
                }
            }
        }
    }

    private void updateTabSelection(int currentTab) {

        enableBottomNavClick = false;
        bottomTabLayout.setCurrentItem(currentTab);
        enableBottomNavClick = true;
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case FragNavController.TAB1:
                return ProfileFragment.newInstance();
            case FragNavController.TAB2:
                return RoutinesFragment.newInstance();
            case FragNavController.TAB3:
                return ExercisesListFragment.newInstance();
            //return ExercisesFragment.newInstance();//R.id.frameLayout_main_activity, R.layout.fragment_exercises);
            case FragNavController.TAB4:
                //return PerformFragment.newInstance();
                return ExercisesFragment.newInstance();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {
            updateToolbar();
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {
            updateToolbar();
        }
    }

    private void updateToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }


    //    private void updateToolbarTitle(int position){
    //
    //
    //        getSupportActionBar().setTitle(TABS[position]);
    //
    //    }

    public void updateToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void startTimerNotificationService(View v, int headerIndex, int setIndex, int minutes, int seconds) {
        ((MyApplication) getApplication()).startTimerNotificationService(v, headerIndex, setIndex, minutes, seconds);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver(restTimerReceiver, new IntentFilter(TimerNotificationService.BROADCAST_INTENT_NAME));
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(restTimerReceiver);
    }
}

