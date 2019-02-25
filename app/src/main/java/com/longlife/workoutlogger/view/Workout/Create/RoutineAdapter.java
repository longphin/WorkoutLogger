/*
 * Created by Longphi Nguyen on 1/17/19 9:40 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/17/19 9:40 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.GetResource;
import com.longlife.workoutlogger.view.Exercises.IExerciseListCallbackBase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<exerciseItemInRoutine> data = new ArrayList<>();
    private IExerciseListCallbackBase simpleCallback;

    RoutineAdapter(IExerciseListCallbackBase simpleCallback) {
        this.simpleCallback = simpleCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new exerciseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof exerciseViewHolder) {
            onBindExerciseViewHolder((exerciseViewHolder) holder, position);
        }
    }

    private void onBindExerciseViewHolder(exerciseViewHolder holder, int position) {
        holder.setName(data.get(position).getName());
        holder.setDescrip(GetResource.getStringResource(simpleCallback.getContext(), R.string.WorkoutExerciseDescription, data.get(position).getNumberOfSets()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void addExercise(exerciseItemInRoutine ex) {
        data.add(ex);
        notifyItemInserted(data.size() - 1);
    }

    public static class exerciseItemInRoutine {
        private Long idRoutineExercise;
        private Long idRoutine;

        public exerciseItemInRoutine(Long idRoutine, Long idExercise, String name, int numberOfSets) {
            this.idRoutine = idRoutine;
            this.idExercise = idExercise;
            this.name = name;
            this.numberOfSets = numberOfSets;
        }

        public Long getIdRoutine() {
            return idRoutine;
        }

        public void setIdRoutine(Long idRoutine) {
            this.idRoutine = idRoutine;
        }

        private Long idExercise;
        private String name;
        private int numberOfSets;

        public Long getIdRoutineExercise() {
            return idRoutineExercise;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public void setIdRoutineExercise(Long idRoutineExercise) {
            this.idRoutineExercise = idRoutineExercise;
        }
    }

    private class exerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView descripView;

        exerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.txt_exerciseName);
            descripView = itemView.findViewById(R.id.txt_exerciseDescrip);
        }

        public void setName(String name) {
            nameView.setText(name);
        }

        public void setDescrip(String descrip) {
            descripView.setText(descrip);
        }
    }
}
