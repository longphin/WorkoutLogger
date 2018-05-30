package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

public class ExercisesOverviewViewModel extends ViewModel {
    private Repository repo;

    // The Observable that will emit a value whenever the "add routine" button is clicked.
    // Views can listen to the stream to find out if that button is clicked.
    public BehaviorSubject<Boolean> addNewExercise = BehaviorSubject.createDefault(false);

    ///
    /// Constructors
    ///
    public ExercisesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public List<Exercise> getExercises() {
        return (repo.getExercises());
    }

    public Boolean startCreateFragment() // [TODO] probably better to change this to return a Completable
    {
        addNewExercise.onNext(true);
        return (true);
    }
}
