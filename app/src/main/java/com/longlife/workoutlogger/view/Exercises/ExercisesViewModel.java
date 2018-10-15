package com.longlife.workoutlogger.view.Exercises;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Status;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;
import com.longlife.workoutlogger.view.Exercises.Helper.ExerciseLocked;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ExercisesViewModel
        extends ViewModel {

    private final static String TAG = ExercisesViewModel.class.getSimpleName();

    private final Response<Exercise> exerciseInsertedResponse = new Response<>();

    // Observable for when requesting list of all exercises.
    private final Response<List<Exercise>> loadExercisesResponse = new Response<>();
    // Observable for when an exercise was locked.
    private final PublishSubject<ExerciseLocked> exerciseLockedObservable = PublishSubject.create();
    // Observable for when an exercise is edited.
    private final PublishSubject<Exercise> exerciseEditedObservable = PublishSubject.create();
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

    public PublishSubject<Exercise> getExerciseEditedObservable() {
        return exerciseEditedObservable;
    }

    public Observable<Response<Exercise>> getExerciseInsertedResponse() {
        return exerciseInsertedResponse.getObservable();
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

    public Observable<Response<List<Exercise>>> getLoadExercisesResponse() {
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

    public void addDeletedExercise(Exercise ex, int pos) {
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

        disposables.add(repo.getExercises()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> {
                    Log.d(TAG, "loading exercises: loading... ");
                    loadExercisesResponse.setLoading();
                })
                .subscribe((List<Exercise> ex) -> {
                            // sort the list of exercises //[TODO] Set the comparator to what the user chooses
                            //Collections.sort(ex, ExerciseComparators.getDefaultComparator());
                            //this.exercises = ex;
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
        disposables.add(repo.insertExercise(exercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        insertedExercise -> {
                            exerciseInsertedResponse.setSuccess(insertedExercise);
                        },
                        throwable -> {
                        }
                ));
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

    public void updateExercise(Exercise exercise) {
        disposables.add(repo.updateExercise(exercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (Exercise updatedExercise) -> {
                            //exerciseEditedResponse.setSuccess(exercise);
                            exerciseEditedObservable.onNext(updatedExercise);
                        },
                        throwable -> {
                        }
                ));
    }
}
