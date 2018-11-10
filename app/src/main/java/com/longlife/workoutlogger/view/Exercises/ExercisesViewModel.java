package com.longlife.workoutlogger.view.Exercises;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Status;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseLocked;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ExercisesViewModel
        extends ViewModel {

    private final static String TAG = ExercisesViewModel.class.getSimpleName();

    // Observable for when an exercise is inserted.
    private final PublishSubject<Exercise> exerciseInsertedObservable = PublishSubject.create();
    // Observable for when requesting list of all exercises.
    private final Response<List<ExerciseShort>> loadExercisesResponse = new Response<>();
    // Observable for when an exercise was locked.
    private final PublishSubject<ExerciseLocked> exerciseLockedObservable = PublishSubject.create();
    // Observable for when an exercise is edited.
    private final PublishSubject<ExerciseUpdated> exerciseEditedObservable = PublishSubject.create();
    // Observable for when an exercise is unhidden.
    private final PublishSubject<DeletedExercise> exerciseRestoredObservable = PublishSubject.create();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Queue<DeletedExercise> exercisesToDelete = new LinkedList<>();
    private Repository repo;

    // Protected
    public ExercisesViewModel(@NonNull Repository repo) {
        this.repo = repo;
    }


    @Override
    public void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public Maybe<SessionExercise> getLatestExerciseSession(Long idExercise) {
        return repo.getLatestExerciseSession(idExercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public PublishSubject<ExerciseUpdated> getExerciseEditedObservable() {
        return exerciseEditedObservable;
    }

    public PublishSubject<Exercise> getExerciseInsertedObservable() {
        return exerciseInsertedObservable;
    }

    public PublishSubject<ExerciseLocked> getExerciseLockedObservable() {
        return exerciseLockedObservable;
    }

    public PublishSubject<DeletedExercise> getExerciseRestoredObservable() {
        return exerciseRestoredObservable;
    }

    public DeletedExercise getFirstDeletedExercise() {
        return exercisesToDelete.poll();
    }

    public Observable<Response<List<ExerciseShort>>> getLoadExercisesResponse() {
        return loadExercisesResponse.getObservable();
    }

    public Single<ExerciseSessionWithSets> getSessionExerciseWithSets(Long idSessionExercise) {
        return repo.getSessionExerciseWithSets(idSessionExercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Exercise> getExerciseFromId(Long idExercise) {
        return repo.getExerciseFromId(idExercise);
    }

    public Single<ExerciseUpdated> getExerciseUpdatableFromId(Long idExercise) {
        return repo.getExerciseUpdatableFromId(idExercise);
    }

    public void addDeletedExercise(ExerciseShort ex, int pos) {
        exercisesToDelete.add(new DeletedExercise(ex, pos));
    }

    // Get observable for all exercise names.
    public Single<List<String>> loadExerciseNames() {
        return repo.getExercisesNames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void loadExercises() {
        Log.d(TAG, "loadExercises()");
        if (loadExercisesResponse.getStatus() == Status.LOADING)
            return;

        disposables.add(repo.getExerciseShort()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> {
                    Log.d(TAG, "loading exercises: loading... ");
                    loadExercisesResponse.setLoading();
                })
                .subscribe((List<ExerciseShort> ex) -> {
                            Log.d(TAG, "loading exercises: success... ");
                            loadExercisesResponse.setSuccess(ex);
                        },
                        throwable ->
                        {
                            Log.d(TAG, "loading exercises: error... ");
                            loadExercisesResponse.setError(throwable);
                        }
                )
        );
    }

    public void updateLockedStatus(Long idExercise, boolean lockedStatus) {
        Completable.fromAction(() -> repo.updateLockedStatus(idExercise, lockedStatus))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        exerciseLockedObservable.onNext(new ExerciseLocked(idExercise, lockedStatus));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                    }
                });
    }

    public void insertExercise(Exercise exercise) {
        repo.insertExercise(exercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Exercise>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onSuccess(Exercise exercise) {
                                   exerciseInsertedObservable.onNext(exercise);
                               }

                               @Override
                               public void onError(Throwable e) {

                               }
                           }
                );
    }

    public void restoreExercise(DeletedExercise deletedExercise) {
        setExerciseHiddenStatus(deletedExercise.getExercise().getIdExercise(), false);
        exerciseRestoredObservable.onNext(deletedExercise);
    }

    public void setExerciseHiddenStatus(Long idExercise, boolean isHidden) {
        Completable.fromAction(() -> repo.setExerciseHiddenStatus(idExercise, isHidden))
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
                        e.getMessage();
                    }
                });
    }

    public Single<SessionExercise> insertNewSessionForExercise(Long idExercise, String note) {
        return repo.insertNewSessionForExercise(idExercise, note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void updateExerciseShort(ExerciseUpdated exercise) {
        Completable.fromAction(() -> repo.updateExercise(exercise))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        exerciseEditedObservable.onNext(exercise);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
