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
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<exerciseItemInRoutine> data = new ArrayList<>();
    private IExerciseListCallbackBase simpleCallback;
    private IRoutineExerciseListCallback routineExerciseListCallback;

    RoutineAdapter(IExerciseListCallbackBase simpleCallback, IRoutineExerciseListCallback routineExerciseListCallback) {
        this.simpleCallback = simpleCallback;
        this.routineExerciseListCallback = routineExerciseListCallback;
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

        if (holder.getMoreOptionsView() != null) {
            holder.getMoreOptionsView().setOnClickListener(view -> {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(simpleCallback.getContext(), holder.getMoreOptionsView());
                //inflating menu from xml resource
                popup.inflate(R.menu.routineexercise_options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    int currentPosition = holder.getAdapterPosition();

                    Long idRoutineExerciseInteracted = data.get(currentPosition).getIdRoutineExercise();
                    switch (item.getItemId()) {
                        case R.id.menu_routineexercise_edit:
                            //handle menu1 click
                            routineExerciseListCallback.routineExerciseEdit(idRoutineExerciseInteracted);
                            return true;
                        case R.id.menu_routineexercise_delete:
                            routineExerciseListCallback.routineExerciseDelete(idRoutineExerciseInteracted);
                            data.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
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
        return data.size();
    }

    void addExercise(exerciseItemInRoutine ex) {
        data.add(ex);
        notifyItemInserted(data.size() - 1);
    }

    public void addExercises(List<exerciseItemInRoutine> exercisesToAddIntoRoutine) {
        int sizeBeforeAddedItems = data.size();
        data.addAll(exercisesToAddIntoRoutine);
        notifyItemRangeInserted(sizeBeforeAddedItems, exercisesToAddIntoRoutine.size());
    }

    public void updateSetCountForExercise(Long idRoutineExercise, int size) {
        for (int i = 0; i < data.size(); i++) {
            exerciseItemInRoutine exercise = data.get(i);
            if (exercise.getIdRoutineExercise().equals(idRoutineExercise)) {
                exercise.setNumberOfSets(size);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public static class exerciseItemInRoutine {
        private Long idRoutineExercise;
        private Long idRoutine;
        private Long idExercise;
        private String name;
        private int numberOfSets;

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

        public Long getIdRoutineExercise() {
            return idRoutineExercise;
        }

        public void setIdRoutineExercise(Long idRoutineExercise) {
            this.idRoutineExercise = idRoutineExercise;
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
    }

    private class exerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView descripView;
        private TextView optionsView;

        exerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.txt_exerciseName);
            descripView = itemView.findViewById(R.id.txt_exerciseDescrip);
            optionsView = itemView.findViewById(R.id.txt_exercise_moreOptions);
        }

        public void setName(String name) {
            nameView.setText(name);
        }

        public void setDescrip(String descrip) {
            descripView.setText(descrip);
        }

        public TextView getMoreOptionsView() {
            return optionsView;
        }
    }
}
