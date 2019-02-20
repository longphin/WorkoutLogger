/*
 * Created by Longphi Nguyen on 2/17/19 2:23 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/17/19 2:23 PM.
 */

package com.longlife.workoutlogger.view.Workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Workout.WorkoutProgramShort;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {
    private List<WorkoutProgramShort> workoutList = new ArrayList<>();

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_list, parent, false);
        return new WorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        final WorkoutProgramShort workout = workoutList.get(position);
        holder.setName(workout.getName());
        holder.setDescrip(workout.getRoutineConcatenated());
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public void setData(List<WorkoutProgramShort> workoutPrograms) {
        this.workoutList = workoutPrograms;
        notifyDataSetChanged();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView descripView;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.txt_workout_name);
            descripView = itemView.findViewById(R.id.txt_workout_descrip);
        }

        public void setName(String name) {
            nameView.setText(name);
        }

        public void setDescrip(String descrip) {
            descripView.setText(descrip);
        }
    }
}
