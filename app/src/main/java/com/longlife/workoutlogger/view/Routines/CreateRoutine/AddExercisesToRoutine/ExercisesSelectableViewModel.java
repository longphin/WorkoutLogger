/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Status;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.utils.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExercisesSelectableViewModel
        extends ViewModel {

    private final static String TAG = ExercisesSelectableViewModel.class.getSimpleName();

    private final Response<List<ExerciseShort>> addExercisesToRoutine = new Response<>(); //[TODO] No need to make this a Response<>
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Set<Long> selectedIdExercises = new HashSet<>();
    private Repository repo;

    public ExercisesSelectableViewModel(Repository repo) {
        this.repo = repo;
    }


    public Observable<Response<List<ExerciseShort>>> getAddExercisesToRoutineResponse() {
        return addExercisesToRoutine.getObservable();
    }

    void clearIdSelectedExercises() {
        selectedIdExercises.clear();
    }

    boolean isIdSelected(Long idExercise) {
        return selectedIdExercises.contains(idExercise);
    }

    void removeSelectedExercise(Long idExercise) {
        selectedIdExercises.remove(idExercise);
    }

    void addSelectedExercise(Long idExercise) {
        selectedIdExercises.add(idExercise);
    }

    void addExercisesToRoutine() {
        Log.d(TAG, "addExercisesToRoutine()");
        if (addExercisesToRoutine.getStatus() == Status.LOADING)
            return;
        Log.d(TAG, "addExercisesToRoutine() continued");
        disposables.add(repo.getExerciseShortFromId(selectedIdExercises)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> addExercisesToRoutine.setLoading())
                .subscribe(exercises ->
                        {
                            addExercisesToRoutine.setSuccess(exercises);
                        },
                        throwable -> addExercisesToRoutine.setError(throwable)
                )
        );
    }
}
