/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:25 PM.
 */

package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.view.Exercises.ExercisesListAdapter;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewHolder;

public class ExercisesSelectableAdapter
        extends ExercisesListAdapter {
    private final static String TAG = ExercisesSelectableAdapter.class.getSimpleName();
    private IExercisesSelectableAdapterCallback exercisesSelectableCallback;

    ExercisesSelectableAdapter(IClickExercise clickExerciseCallback, IExercisesSelectableAdapterCallback selectableAdapterCallback) {
        super(clickExerciseCallback);
        this.exercisesSelectableCallback = selectableAdapterCallback;
    }

    @Override
    public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_selectable, parent, false);

        return (new ExercisesViewHolder(v));
    }

    @Override
    protected void bindMyViewHolder(ExercisesViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final ExerciseShort ex = exercises.get(position);

        // Get the checkbox
        CheckBox selectedCheckBox = holder.getSelectedCheckBox();
        if (selectedCheckBox != null) {
            Long idCurrentlySelected = ex.getIdExercise();
            boolean isCurrentlySelected = exercisesSelectableCallback.isIdSelected(idCurrentlySelected);//viewModel.isIdSelected(idCurrentlySelected);
            if (isCurrentlySelected) {
                holder.setSelectedCheckBox(true);
                Log.d(TAG, "Is " + String.valueOf(idCurrentlySelected) + " selected: Yes");
            } else {
                holder.setSelectedCheckBox(false); // This can happen if the holder view was restored (such as when item was deleted, then undone).
                Log.d(TAG, "Is " + String.valueOf(idCurrentlySelected) + " selected: No");
            }

            selectedCheckBox.setOnClickListener(
                    view ->
                    {
                        Long id = ex.getIdExercise();
                        boolean isSelected = exercisesSelectableCallback.isIdSelected(id);
                        if (isSelected) {
                            exercisesSelectableCallback.removeSelectedExcercise(id);
                        } else {
                            exercisesSelectableCallback.addSelectedExercise(id);
                        }
                        Log.d(TAG, String.valueOf(id) + " is " + (!isSelected ? "selected" : "unselected"));
                    }
            );
        }
    }

    public interface IExercisesSelectableAdapterCallback {
        boolean isIdSelected(Long idExercise);

        void removeSelectedExcercise(Long idExercise);

        void addSelectedExercise(Long idExercise);
    }

}
