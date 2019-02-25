/*
 * Created by Longphi Nguyen on 2/18/19 3:24 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/18/19 2:44 PM.
 */

package com.longlife.workoutlogger.dataViewModel;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.Workout.WorkoutProgramShort;
import com.longlife.workoutlogger.view.Workout.Create.RoutineAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    public void insertExerciseForRoutine(RoutineAdapter.exerciseItemInRoutine ex) {
        repo.insertExerciseForRoutine(ex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RoutineAdapter.exerciseItemInRoutine>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(RoutineAdapter.exerciseItemInRoutine addedRoutineExercise) {
                        exerciseToInsertObservable.onNext(addedRoutineExercise);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public Single<Long> createNewWorkoutProgram() {
        return repo.createWorkoutProgram()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<WorkoutProgramShort>> getWorkoutShortList() {
        return repo.getWorkoutShortList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
