package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.List;

public class ExercisesOverviewViewModel extends ViewModel {
    private Repository repo;

    ///
    /// Constructors
    ///
    public ExercisesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public List<Exercise> getExercises() {
        return (repo.getExercises());
    }
}
