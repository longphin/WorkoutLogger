/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:25 PM.
 */

package com.longlife.workoutlogger.view;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.dataViewModel.WorkoutViewModel;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Profile.ProfileViewModel;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableViewModel;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomViewModelFactory
        implements ViewModelProvider.Factory {

    private final Repository repo;

    @Inject
    public CustomViewModelFactory(Repository repo) {
        this.repo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RoutinesViewModel.class)) {
            return ((T) new RoutinesViewModel(repo));
        } else if (modelClass.isAssignableFrom(ExercisesViewModel.class)) {
            return ((T) new ExercisesViewModel(repo));
        } else if (modelClass.isAssignableFrom(ExercisesSelectableViewModel.class)) {
            return ((T) new ExercisesSelectableViewModel(repo));
        } else if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return ((T) new ProfileViewModel(repo));
        } else if (modelClass.isAssignableFrom(WorkoutViewModel.class)) {
            return ((T) new WorkoutViewModel(repo));
        } else {
            throw new IllegalArgumentException("ViewModel not found");
        }

    }
}

