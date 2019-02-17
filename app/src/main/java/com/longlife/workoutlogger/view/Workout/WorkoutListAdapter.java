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
import com.longlife.workoutlogger.model.WorkoutProgram;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {
    private List<WorkoutProgram> workoutList = new ArrayList<>();

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_list, parent, false);
        return new WorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.setName(workoutList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public void setData(List<WorkoutProgram> workoutPrograms) {
        this.workoutList = workoutPrograms;
        notifyDataSetChanged();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.txt_workout_name);
        }

        public void setName(String name) {
            nameView.setText(name);
        }
    }
}
