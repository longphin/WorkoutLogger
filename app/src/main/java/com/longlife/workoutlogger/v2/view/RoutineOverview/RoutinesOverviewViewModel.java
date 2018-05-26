package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

public class RoutinesOverviewViewModel extends ViewModel {
    private Repository repo;

    // The Observable that will emit a value whenever the "add routine" button is clicked.
    // Views can listen to the stream to find out if that button is clicked.
    public BehaviorSubject<Boolean> addNewRoutine = BehaviorSubject.createDefault(false);

    ///
    /// Constructors
    ///
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
