package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.BaseActivity;
import com.longlife.workoutlogger.v2.view.Exercise_Insert.ExerciseCreateFragment;

import io.reactivex.observers.DisposableObserver;

public class ExercisesOverviewActivity extends BaseActivity {
    private static final String TAG = "ExercisesOverview_ACT";

    private ExercisesOverviewViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_overview);

        ((MyApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(ExercisesOverviewViewModel.class);

        // Add initial fragments.
        initializeFragments();

        // Add listener for when user wants to add Exercise.
        initializeExerciseCreateListener();
    }

    public void initializeFragments() {
        ExercisesOverviewFragment fragment = (ExercisesOverviewFragment) manager.findFragmentByTag(ExercisesOverviewFragment.TAG);
        if (fragment == null) {
            fragment = ExercisesOverviewFragment.newInstance(this);
        }

        addFragmentToActivity(manager, fragment, R.id.root_exercises_overview, ExercisesOverviewFragment.TAG);
    }

    // Initialize a subscriber that listens to when it is desired to open a RoutineCreateFragment.
    public void initializeExerciseCreateListener() {
        DisposableObserver<Boolean> observer1 =
                new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            ExerciseCreateFragment fragment = (ExerciseCreateFragment) manager.findFragmentByTag(ExerciseCreateFragment.TAG);
                            if (fragment == null) {
                                fragment = ExerciseCreateFragment.newInstance(getApplicationContext());
                                manager.beginTransaction()
                                        .replace(R.id.root_exercises_overview, fragment, ExerciseCreateFragment.TAG)
                                        .addToBackStack(null)
                                        .commit();
                            }

                            addFragmentToActivity(manager, fragment, R.id.root_exercises_overview, ExerciseCreateFragment.TAG);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
        viewModel.addNewExercise.subscribe(observer1);
        addDisposable(observer1);
    }
}
