package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.ResponseBoolean;
import com.longlife.workoutlogger.v2.utils.ResponseListExercise;
import com.longlife.workoutlogger.v2.utils.ResponseLong;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExercisesOverviewViewModel extends ViewModel {
    private final static String TAG = "ExercisesOverviewVM";
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ResponseListExercise> getResponse = new MutableLiveData<>();
    private final MutableLiveData<ResponseBoolean> startExerciseCreateFragmentResponse = new MutableLiveData<>();

    public final ResponseLong insertResponse = new ResponseLong();

    // The Observable that will emit a value whenever the "add routine" button is clicked.
    // Views can listen to the stream to find out if that button is clicked.
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

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void startCreateFragment() {
        disposables.add(Observable.just("this observable emits data whenever the 'Add new Exercise button' is clicked")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> startExerciseCreateFragmentResponse.setValue(ResponseBoolean.loading()))
                .subscribe(b -> startExerciseCreateFragmentResponse.setValue(ResponseBoolean.success(true)),
                        throwable -> startExerciseCreateFragmentResponse.setValue(ResponseBoolean.error(throwable))));

    }

    public void loadExercises() {
        disposables.add(repo.getExercises()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getResponse.setValue(ResponseListExercise.loading()))
                .subscribe(ex ->
                        {
                            this.exercises = ex;
                            getResponse.setValue(ResponseListExercise.success(ex));
                        },
                        throwable -> getResponse.setValue(ResponseListExercise.error(throwable)))
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

    public Observable<ResponseLong> getInsertResponse() {
        return insertResponse.getObservable();
    }

    public MutableLiveData<ResponseListExercise> loadResponse() {
        return getResponse;
    }

    public MutableLiveData<ResponseBoolean> newExerciseResponse() {
        return startExerciseCreateFragmentResponse;
    }
}
