/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;

import java.util.List;

import androidx.appcompat.widget.PopupMenu;

public class ExercisesListRemakeAdapter extends ExercisesListAdapterBase {
    private IExerciseListCallback callback;

    public ExercisesListRemakeAdapter(IExerciseListCallbackBase callback, List<IExerciseListable> exercises, String query) {
        super(exercises, query);
        if (callback instanceof IExerciseListCallback)
            this.callback = (IExerciseListCallback) callback;
    }

    @Override
    protected int exerciseItemLayout() {
        return R.layout.item_exercise;
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
                    popup.inflate(R.menu.exercise_options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        int currentPosition = holder.getAdapterPosition();

                        switch (item.getItemId()) {
                            case R.id.menu_exercise_edit:
                                //handle menu1 click
                                callback.exerciseEdit(data.get(currentPosition).id());
                                return true;
                            case R.id.menu_exercise_perform:
                                //handle menu1 click
                                callback.exercisePerform((exerciseItem) data.get(currentPosition));//ex.getIdExercise(), ex.getName());
                                return true;
                            case R.id.menu_exercise_delete:
                                Long idExerciseToDelete = data.get(currentPosition).id();
                                ExerciseShort exerciseToDelete = getExerciseById(idExerciseToDelete);
                                callback.exerciseDelete(exerciseToDelete);
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
        void exercisePerform(exerciseItem ex);//Long idExercise, String exerciseName);

        void exerciseDelete(ExerciseShort exerciseToDelete);
    }
}
