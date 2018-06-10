package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.Response;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExercisesOverviewViewModel extends ViewModel {
    private final static String TAG = ExercisesOverviewViewModel.class.getSimpleName();
    private final CompositeDisposable disposables = new CompositeDisposable();

    // Observable for when inserting a new exercise.
    private final Response<Long> insertResponse = new Response<>();
    // Observable for when requesting list of all exercises.
    private final Response<List<Exercise>> loadResponse = new Response<>();
    // Observable for when to start creating a new exercise fragment.
    private final Response<Boolean> startCreateFragmentResponse = new Response<>();

    private Repository repo;
    private List<Exercise> exercises;

    ///
    /// Constructors
    ///
    public ExercisesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public Single<List<Exercise>> getExercises() {
        return (repo.getExercises());
    }

    @Override
    public void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void startCreateFragment() {
        disposables.add(Observable.just(true) // this emitted value does not matter
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> startCreateFragmentResponse.setLoading())
                .subscribe(b -> startCreateFragmentResponse.setSuccess(b),
                        throwable -> startCreateFragmentResponse.setError(throwable))
        );
    }

    public void loadExercises() {
        disposables.add(repo.getExercises()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> loadResponse.setLoading())
                .subscribe(ex -> {
                            this.exercises = ex;
                            loadResponse.setSuccess(ex);
                        },
                        throwable -> loadResponse.setError(throwable))
        );
    }

    public void insertExercise(Exercise ex) {
        disposables.add(
                repo.insertExercise(ex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> insertResponse.setLoading())
                        .subscribe(id -> insertResponse.setSuccess(id),
                                throwable -> insertResponse.setError(throwable))
        );
    }

    // Reference: Ala Hammad - https://medium.com/@alahammad/database-with-room-using-rxjava-764ee6124974
    public void deleteExercise(Exercise ex)
    {
        Completable.fromAction(() -> repo.deleteExercise(ex))
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

    public Observable<Response<Long>> getInsertResponse() {
        return insertResponse.getObservable();
    }

    public Observable<Response<List<Exercise>>> getLoadResponse() {
        return loadResponse.getObservable();
    }

    public Observable<Response<Boolean>> startCreateFragmentResponse() {
        return startCreateFragmentResponse.getObservable();
    }

    ///
    /// GETTERS
    ///
    public List<Exercise> getCachedExercises() {
        return exercises;
    }

    ///
    /// UPDATE
    ///
    public void updateFavorite(int idExercise, boolean favorited) {
        Completable.fromAction(() -> repo.updateFavorite(idExercise, favorited))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Update successful.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                    }
                });
    }
}
