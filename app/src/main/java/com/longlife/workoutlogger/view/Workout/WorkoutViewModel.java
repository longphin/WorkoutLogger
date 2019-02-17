/*
 * Created by Longphi Nguyen on 2/17/19 2:56 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/17/19 1:26 PM.
 */

package com.longlife.workoutlogger.view.Workout;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.WorkoutProgram;
import com.longlife.workoutlogger.view.Workout.Create.RoutineAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class WorkoutViewModel extends ViewModel {
    private final PublishSubject<RoutineAdapter.exerciseItemInRoutine> exerciseToInsertObservable = PublishSubject.create();
    private Repository repo;

    public WorkoutViewModel(@NonNull Repository repo) {
        this.repo = repo;
    }

    public PublishSubject<RoutineAdapter.exerciseItemInRoutine> getExerciseToInsertObservable() {
        return exerciseToInsertObservable;
    }

    public void insertExercise(RoutineAdapter.exerciseItemInRoutine ex) {
        exerciseToInsertObservable.onNext(ex); // [TODO] insert into database
    }

    public Single<Long> createNewWorkoutProgram() {
        return repo.createWorkoutProgram()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<WorkoutProgram> getFirstUnsavedWorkout() {
        return repo.getFirstUnsavedWorkout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<WorkoutProgram>> getWorkoutList() {
        return repo.getWorkoutList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
