/*
 * Created by Longphi Nguyen on 1/17/19 9:40 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/17/19 9:40 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<exerciseItemInRoutine> data = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new exerciseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addExercise(Long idExercise) {
        data.add(new exerciseItemInRoutine(idExercise, 1));
        notifyItemInserted(data.size() - 1);
    }

    private class exerciseItemInRoutine {
        Long idExercise;
        private int numberOfSets;

        public exerciseItemInRoutine(Long idExercise, int numberOfSets) {
            this.idExercise = idExercise;
            this.numberOfSets = numberOfSets;
        }

        public Long getIdExercise() {
            return idExercise;
        }

        public void setIdExercise(Long idExercise) {
            this.idExercise = idExercise;
        }

        public int getNumberOfSets() {
            return numberOfSets;
        }

        public void setNumberOfSets(int numberOfSets) {
            this.numberOfSets = numberOfSets;
        }
    }

    private class exerciseViewHolder extends RecyclerView.ViewHolder {
        public exerciseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
