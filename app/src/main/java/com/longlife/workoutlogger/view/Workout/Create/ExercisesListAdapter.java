/*
 * Created by Longphi Nguyen on 1/13/19 7:00 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/13/19 6:44 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.ExerciseListExerciseViewHolder;
import com.longlife.workoutlogger.view.Exercises.ExercisesListAdapterBase;
import com.longlife.workoutlogger.view.Exercises.IExerciseListCallbackBase;

import java.util.List;

import androidx.appcompat.widget.PopupMenu;

public class ExercisesListAdapter extends ExercisesListAdapterBase {
    private IExerciseListCallback callback;

    ExercisesListAdapter(IExerciseListCallback callback, List<IExerciseListable> exercises, String query) {
        super(exercises, query);
        this.callback = callback;
    }

    @Override
    protected int exerciseItemLayout() {
        return R.layout.item_workout_create_exercise;
    }

    @Override
    protected void onBindExerciseViewHolder(ExerciseListExerciseViewHolder holder, int position) {
        if (data.get(position) instanceof exerciseItem) {
            holder.getNameTextView().setText((data.get(position)).toString());

            // Create listener for the "more options" button. credit: Shaba Aafreen @https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item
            if (holder.getMoreOptionsView() != null) {
                holder.getMoreOptionsView().setOnClickListener(view -> {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(callback.getContext(), holder.getMoreOptionsView());
                    //inflating menu from xml resource
                    popup.inflate(R.menu.workout_exercise_options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        int currentPosition = holder.getAdapterPosition();

                        switch (item.getItemId()) {
                            case R.id.menu_exercise_edit:
                                //handle menu1 click
                                callback.exerciseEdit(data.get(currentPosition).id());
                                return true;
                            case R.id.menu_exercise_add_to_routine:
                                callback.addExerciseToRoutine(data.get(currentPosition).id(), data.get(currentPosition).toString());
                                return true;
                            default:
                                return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                });
            }
        }
    }

    public interface IExerciseListCallback extends IExerciseListCallbackBase {
        void addExerciseToRoutine(Long idExercise, String exerciseName);
    }
}
