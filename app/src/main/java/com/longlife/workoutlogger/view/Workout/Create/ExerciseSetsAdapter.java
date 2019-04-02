/*
 * Created by Longphi Nguyen on 3/27/19 8:02 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/27/19 8:02 AM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.Item_Add_ViewHolder;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseSetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SETTYPE = 1;
    private static final int ADDTYPE = 0;
    private List<ExerciseSet> sets = new ArrayList<>(); // This is the current list of sets, both existing and new.
    private List<ExerciseSet> deletedSets = new ArrayList<>(); // This is a list of sets to be deleted from the database.

    public List<ExerciseSet> getSets() {
        return sets;
    }

    public List<ExerciseSet> getDeletedSets() {
        return deletedSets;
    }

    public void deleteSet(int position) {
        ExerciseSet setToDelete = sets.get(position);
        sets.remove(position);
        if (setToDelete.getIdExerciseSet() != null)
            deletedSets.add(setToDelete);
    }

    public void setData(List<ExerciseSet> data) {
        sets = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SETTYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_exercise_set, parent, false);
            return new SetsViewHolder(v);
        }

        // Else, create an "add item" view holder.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);
        return new Item_Add_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Item_Add_ViewHolder) {
            onBindAddViewHolder((Item_Add_ViewHolder) holder, position);
            return;
        }

        if (holder instanceof SetsViewHolder) {
            onBindSetsViewHolder((SetsViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < sets.size()) {
            return SETTYPE;
        }
        return ADDTYPE;
    }

    @Override
    public int getItemCount() {
        return sets.size() + 1;
    }

    private void onBindAddViewHolder(Item_Add_ViewHolder holder, int position) {
        holder.getAddButton().setOnClickListener(v ->
        {
            addSet();
        });
    }

    private void onBindSetsViewHolder(SetsViewHolder holder, int position) {
        holder.getSetNumberView().setText(String.valueOf(position));
    }

    private void addSet() {
        int positionToAdd = sets.size();
        ExerciseSet setToAdd = new ExerciseSet();
        sets.add(setToAdd);
        notifyItemInserted(positionToAdd);
    }

    private class SetsViewHolder extends RecyclerView.ViewHolder {
        private TextView setNumberView;

        public SetsViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumberView = itemView.findViewById(R.id.txt_setNumber);
        }

        public TextView getSetNumberView() {
            return setNumberView;
        }
    }
}
