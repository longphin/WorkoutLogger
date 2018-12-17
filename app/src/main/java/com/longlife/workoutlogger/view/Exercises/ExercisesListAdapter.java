/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:25 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseLocked;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;

import java.util.ArrayList;
import java.util.List;

public abstract class ExercisesListAdapter
        extends RecyclerView.Adapter<ExercisesViewHolder> {
    protected List<ExerciseShort> exercises = new ArrayList<>();
    private IClickExercise adapterCallback;

    public ExercisesListAdapter(IClickExercise adapterCallback) {
        setAdapterCallback(adapterCallback);
    }

    private void setAdapterCallback(IClickExercise adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    @Override
    public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);

        return (new ExercisesViewHolder(v));
    }

    @Override
    public void onBindViewHolder(ExercisesViewHolder holder, int pos) {
        // Bind common elements.
        bindMyViewHolderCommon(holder, pos);

        // Bind any unique elements.
        bindMyViewHolder(holder, pos);
    }

    private void bindMyViewHolderCommon(ExercisesViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        ExerciseShort ex = exercises.get(position);
        // Name
        holder.setNameText(ex.getName() + " (" + String.valueOf(ex.getIdExercise()) + ")");
        // Description
        holder.setDescripText(ex.getNote());
        // Lock icon
        if (ex.isLocked()) {
            holder.setLockedIcon(R.drawable.ic_lock_black_24dp);
        } else {
            holder.setLockedIcon(R.drawable.ic_lock_open_black_24dp);
        }

        holder.getLockedIcon().setOnClickListener(view ->
                {
                    final boolean newLockStatus = !ex.isLocked();
                    adapterCallback.exerciseLocked(ex.getIdExercise(), newLockStatus);

                    if (newLockStatus) {
                        holder.setLockedIcon(R.drawable.ic_lock_black_24dp);
                    } else {
                        holder.setLockedIcon(R.drawable.ic_lock_open_black_24dp);
                    }
                }
        );

        // Edit exercise
        holder.getNameTextView()
                .setOnClickListener(view ->
                {
                    adapterCallback.exerciseClicked(ex.getIdExercise());
                });

        // Create listener for the "more options" button. credit: Shaba Aafreen @https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item
        if (holder.getMoreOptionsView() != null) {
            holder.getMoreOptionsView().setOnClickListener(view -> {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(adapterCallback.getContext(), holder.getMoreOptionsView());
                //inflating menu from xml resource
                popup.inflate(R.menu.exercise_options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_exercise_perform:
                            //handle menu1 click
                            adapterCallback.exercisePerform(ex);//ex.getIdExercise(), ex.getName());
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

    protected abstract void bindMyViewHolder(ExercisesViewHolder holder, int pos);

    @Override
    public int getItemCount() {
        return exercises.size();
    }


    public void setExercises(List<ExerciseShort> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    void exerciseUpdated(ExerciseShort updatedExercise) {
        final Long idExerciseEdited = updatedExercise.getIdExercise();
        // Find where in the adapter this exercise is and notify the change.
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getIdExercise().equals(idExerciseEdited)) {
                exercises.set(i, updatedExercise);
                notifyItemChanged(i);
                return;
            }
        }
    }

    void exerciseLocked(ExerciseLocked exerciseLocked) {
        final Long idExercise = exerciseLocked.getIdExercise();
        final boolean lockStatus = exerciseLocked.isLocked();

        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getIdExercise().equals(idExercise)) {
                exercises.get(i).setLocked(lockStatus);
                return;
            }
        }
    }

    public ExerciseShort getExercise(int position) {
        return exercises.get(position);
    }

    void removeExercise(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    void restoreExercise(ExerciseShort restoredItem, int restoredPosition) {
        exercises.add(restoredPosition, restoredItem);
        notifyItemInserted(restoredPosition);
    }

    void addExercise(ExerciseShort ex) {
        exercises.add(ex);
        notifyItemInserted(exercises.size() - 1);
    }

    int getSwipeDirs(int adapterPosition) {
        if (exercises.get(adapterPosition).isLocked())
            return 0;
        else
            return ItemTouchHelper.RIGHT;
    }

    void clearObjects() {
        adapterCallback = null;
        exercises = null;
    }

    // Interface for when an item is clicked. Should be implemented by the Activity/Fragment to start an edit fragment.
    public interface IClickExercise {
        // When an exercise is clicked, send the clicked exercise.
        void exerciseClicked(Long idExercise);

        void exerciseLocked(Long idExercise, boolean lockStatus);

        void exercisePerform(ExerciseShort ex);//Long idExercise, String exerciseName);

        Context getContext();
    }
}
