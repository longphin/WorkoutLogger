/*
 * Created by Longphi Nguyen on 3/31/19 12:01 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/31/19 12:01 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditRoutineExerciseTestDialog extends DialogBase {
    public static final String TAG = EditRoutineExerciseTestDialog.class.getSimpleName();
    private static final String INPUT_IDROUTINEEXERCISE = "idRoutineExercise";

    public static EditRoutineExerciseTestDialog newInstance(Long idRoutineExercise) {
        Bundle bundle = new Bundle();
        bundle.putLong(INPUT_IDROUTINEEXERCISE, idRoutineExercise);

        EditRoutineExerciseTestDialog fragment = new EditRoutineExerciseTestDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_routineexercise_test_dialog, container, false);
        return mView;
    }
}
