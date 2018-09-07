package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;

import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Status;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.utils.Response;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExercisesSelectableViewModel
        extends ViewModel {

    private final static String TAG = ExercisesSelectableViewModel.class.getSimpleName();

    private final Response<List<Exercise>> addExercisesToRoutine = new Response<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Set<Long> selectedIdExercises = new HashSet<>();
    private Repository repo;

    public ExercisesSelectableViewModel(Repository repo) {
        this.repo = repo;
    }


    public Observable<Response<List<Exercise>>> getAddExercisesToRoutineResponse() {
        return addExercisesToRoutine.getObservable();
    }

    public void clearIdSelectedExercises() {
        selectedIdExercises.clear();
    }

    public boolean isIdSelected(Long idExercise) {
        return selectedIdExercises.contains(idExercise);
    }

    public void removeSelectedExercise(Long idExercise) {
        selectedIdExercises.remove(idExercise);
    }

    public void addSelectedExercise(Long idExercise) {
        selectedIdExercises.add(idExercise);
    }

    public void addExercisesToRoutine() {
        Log.d(TAG, "addExercisesToRoutine()");
        if (addExercisesToRoutine.getStatus() == Status.LOADING)
            return;
        Log.d(TAG, "addExercisesToRoutine() continued");
        disposables.add(repo.getExerciseFromId(selectedIdExercises)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> addExercisesToRoutine.setLoading())
                .subscribe((List<Exercise> exercises) ->
                        {
                            addExercisesToRoutine.setSuccess(exercises);
                        },
                        throwable -> addExercisesToRoutine.setError(throwable)
                )
        );
    }
}
