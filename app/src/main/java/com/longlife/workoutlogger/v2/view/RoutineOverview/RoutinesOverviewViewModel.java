package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

// [TODO] do this a different way. Look up Room and RxJava2
public class RoutinesOverviewViewModel extends ViewModel {
    private Repository repo;

    public RoutinesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public List<Routine> getRoutines() {
        return (repo.getRoutines());
    }
}
