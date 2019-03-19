/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.view.Routines;

import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.Routine.Routine;
import com.longlife.workoutlogger.model.Routine.RoutineShort;
import com.longlife.workoutlogger.model.comparators.RoutineComparators;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Routines.Helper.DeletedRoutine;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;
import com.longlife.workoutlogger.view.Workout.Create.EditRoutineDetailsDialog;
import com.longlife.workoutlogger.view.Workout.Create.RoutineAdapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RoutinesViewModel
        extends ViewModel {

    private final static String TAG = RoutinesViewModel.class.getSimpleName();

    private final CompositeDisposable disposables = new CompositeDisposable();

    // Observable for when inserting a new routine.
    private final Response<Routine> insertResponse = new Response<>();
    // Observable for when requesting list of all routines.
    private final Response<List<Routine>> loadResponse = new Response<>();

    private Repository repo;
    ///
    /// UPDATE
    ///
    private Queue<DeletedRoutine> routinesToDelete = new LinkedList<>();

    ///
    /// Constructors
    ///
    public RoutinesViewModel(Repository repo) {
        this.repo = repo;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        disposables.clear();
    }


    DeletedRoutine getFirstDeletedRoutine() {
        return routinesToDelete.poll();
    }

    public Observable<Response<Routine>> getInsertResponse() {
        return insertResponse.getObservable();
    }

    Observable<Response<List<Routine>>> getLoadResponse() {
        return loadResponse.getObservable();
    }

    void loadRoutines() {
        disposables.add(repo.getRoutines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> loadResponse.setLoading())
                .subscribe((List<Routine> ro) -> {
                            // sort the list of exercises //[TODO] Set the comparator to what the user chooses
                            Collections.sort(ro, RoutineComparators.getDefaultComparator());
                            //this.routines = ro;
                            loadResponse.setSuccess(ro);
                        },
                        throwable -> loadResponse.setError(throwable)
                )
        );
    }

    // Reference: Ala Hammad - https://medium.com/@alahammad/database-with-room-using-rxjava-764ee6124974
    public void deleteRoutine(Routine ro) {
        Completable.fromAction(() -> repo.deleteRoutine(ro))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Delete successful.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                    }
                });
    }

    public void insertRoutineFull(Routine ro, List<RoutineExerciseHelper> reh) {
        disposables.add(
                repo.insertRoutineFull(ro, reh)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> insertResponse.setLoading())
                        .subscribe(insertResponse::setSuccess,
                                insertResponse::setError
                        )
        );
    }

    void addDeletedRoutine(Routine deletedItem, int position) {
        routinesToDelete.add(new DeletedRoutine(deletedItem, position));
    }

    void setRoutineHiddenStatus(Long idRoutine, boolean isHidden) {
        Completable.fromAction(() -> repo.setRoutineAsHidden(idRoutine, isHidden))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onComplete() {

                               }

                               @Override
                               public void onError(Throwable e) {
                               }
                           }
                );
    }

    public Single<Routine> insertRoutineForWorkout(@NonNull Long idWorkout) {
        return repo.insertRoutineForWorkout(idWorkout)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Routine>> getRoutinesForWorkout(Long idWorkout) {
        return repo.getRoutinesForWorkout(idWorkout)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Query("SELECT re.idRoutineExercise, e.idExercise, e.name, e.note, e.locked" +
            " FROM Routine as r" +
            " INNER JOIN RoutineExercise as re on r.idRoutine=re.idRoutine" +
            " INNER JOIN Exercise as e on re.idExercise=e.idExercise" +
            " WHERE r.idRoutine=:idRoutine")
    public Single<List<RoutineAdapter.exerciseItemInRoutine>> getExercisesShortForRoutine(Long idRoutine) {
        return repo.getExercisesShortForRoutine(idRoutine)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void updateSchedule(Long idRoutine, EditRoutineDetailsDialog.RoutineUpdateHelper routineUpdates) {
        Completable.fromAction(() -> repo.updateRoutineSchedule(idRoutine, routineUpdates))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public Single<RoutineShort> getRoutineShortObservable(Long idRoutine) {
        return repo.getRoutineShort(idRoutine)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}

