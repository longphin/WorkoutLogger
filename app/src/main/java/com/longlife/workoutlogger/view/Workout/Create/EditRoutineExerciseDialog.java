/*
 * Created by Longphi Nguyen on 3/24/19 3:06 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/24/19 3:06 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine.ExerciseSet;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class EditRoutineExerciseDialog extends DialogBase {
    public static final String TAG = EditRoutineExerciseDialog.class.getSimpleName();
    private static final String INPUT_IDROUTINEEXERCISE = "idRoutineExercise";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private RoutinesViewModel routineViewModel;
    private Long idRoutineExercise;

    public static EditRoutineExerciseDialog newInstance(Long idRoutineExercise) {
        Bundle bundle = new Bundle();
        bundle.putLong(INPUT_IDROUTINEEXERCISE, idRoutineExercise);

        EditRoutineExerciseDialog fragment = new EditRoutineExerciseDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);

            routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);

            if (getArguments() != null) {
                idRoutineExercise = getArguments().getLong(INPUT_IDROUTINEEXERCISE);
                routineViewModel.getSetsForRoutineExercise(idRoutineExercise)
                        .subscribe(new SingleObserver<List<ExerciseSet>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<ExerciseSet> exerciseSets) {
                                // [TODO] add the list to an adapter.
                                Log.d(TAG, "Loaded sets: " + String.valueOf(exerciseSets.size()));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_routine_exercise_edit_dialog, container, false);
        return mView;
    }
}
