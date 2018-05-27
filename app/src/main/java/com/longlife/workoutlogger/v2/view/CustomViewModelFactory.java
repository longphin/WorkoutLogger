package com.longlife.workoutlogger.v2.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewViewModel;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutinesOverviewViewModel;

import javax.inject.Inject;

public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final Repository repo;

    @Inject
    public CustomViewModelFactory(Repository repo) {
        this.repo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RoutinesOverviewViewModel.class)) {
            return ((T) new RoutinesOverviewViewModel(repo));
        } else if (modelClass.isAssignableFrom(ExercisesOverviewViewModel.class)) {
            return ((T) new ExercisesOverviewViewModel(repo));
        } else {
            throw new IllegalArgumentException("ViewModel not found");
        }

    }
}
