package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.BaseActivity;
import com.longlife.workoutlogger.v2.view.Routine_Insert.RoutineCreateFragment;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class RoutinesOverviewActivity extends BaseActivity {
    private static final String TAG = "RoutineOverview_ACT";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    RoutinesOverviewViewModel viewModel;

    private FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_overview);

        ((MyApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);

        // Initialize RoutinesOverviewFragment.
        initializeFragments();

        // Initialize a subscriber that observes when to start a RoutineCreateFragment.
        initializeRoutineCreateListener();
    }

    public void initializeFragments() {
        RoutinesOverviewFragment fragment = (RoutinesOverviewFragment) manager.findFragmentByTag(RoutinesOverviewFragment.TAG);
        if (fragment == null) {
            fragment = RoutinesOverviewFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutinesOverviewFragment.TAG);
    }

    // Initialize a subscriber that listens to when it is desired to open a RoutineCreateFragment.
    public void initializeRoutineCreateListener() {
        DisposableObserver<Boolean> observer1 =
                new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            RoutineCreateFragment fragment = (RoutineCreateFragment) manager.findFragmentByTag(RoutineCreateFragment.TAG);
                            if (fragment == null) {
                                fragment = RoutineCreateFragment.newInstance();
                                manager.beginTransaction()
                                        .replace(R.id.root_routines_overview, fragment, RoutineCreateFragment.TAG)
                                        .addToBackStack(null)
                                        .commit();
                            }

                            addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutineCreateFragment.TAG);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
        viewModel.addNewRoutine.subscribe(observer1);
        addDisposable(observer1);
    }
}
