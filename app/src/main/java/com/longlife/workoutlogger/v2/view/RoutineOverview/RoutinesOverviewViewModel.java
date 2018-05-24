package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

public class RoutinesOverviewViewModel extends ViewModel {
    private Repository repo;

    public BehaviorSubject<Boolean> addNewRoutine = BehaviorSubject.createDefault(false);

    public RoutinesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public List<Routine> getRoutines() {
        return (repo.getRoutines());
    }

    public Long insertRoutine(Routine routine) {
        return (repo.insertRoutine(routine));
    }

    public Routine getRoutine(int idRoutine) {
        return (repo.getRoutine(idRoutine));
    }

    public Boolean startCreateFragment() // [TODO] probably better to change this to return a Completable
    {
        addNewRoutine.onNext(true);
        return (true);
    }
}
