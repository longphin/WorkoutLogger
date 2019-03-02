/*
 * Created by Longphi Nguyen on 2/17/19 2:23 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/17/19 2:23 PM.
 */

package com.longlife.workoutlogger.view.Workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Workout.WorkoutProgramShort;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {
    private List<WorkoutProgramShort> workoutList = new ArrayList<>();

    private OptionsCallback callback;

    public WorkoutListAdapter(OptionsCallback callback) {
        this.callback = callback;
    }

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

        // Create listener for the "more options" button. credit: Shaba Aafreen @https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item
        if (holder.getMoreOptionsView() != null) {
            holder.getMoreOptionsView().setOnClickListener(view -> {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(callback.getContext(), holder.getMoreOptionsView());
                //inflating menu from xml resource
                popup.inflate(R.menu.workout_options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    int currentPosition = holder.getAdapterPosition();

                    switch (item.getItemId()) {
                        case R.id.menu_workout_edit:
                            //handle menu1 click
                            callback.workoutEdit(workoutList.get(currentPosition).getIdWorkout());
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

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public void setData(List<WorkoutProgramShort> workoutPrograms) {
        this.workoutList = workoutPrograms;
        notifyDataSetChanged();
    }

    public interface OptionsCallback {
        Context getContext();

        void workoutEdit(Long idWorkout);
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView descripView;
        private TextView moreOptions;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.txt_workout_name);
            descripView = itemView.findViewById(R.id.txt_workout_descrip);
            moreOptions = itemView.findViewById(R.id.txt_workout_moreOptions);
        }

        public void setName(String name) {
            nameView.setText(name);
        }

        public void setDescrip(String descrip) {
            descripView.setText(descrip);
        }

        public TextView getMoreOptionsView() {
            return moreOptions;
        }
    }
}
