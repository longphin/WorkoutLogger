/*
 * Created by Longphi Nguyen on 3/27/19 8:02 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/27/19 8:02 AM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.longlife.workoutlogger.AndroidUtils.Item_Add_ViewHolder;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseSetType;
import com.longlife.workoutlogger.model.Routine.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseSetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SETTYPE = 1;
    private static final int ADDTYPE = 0;
    private List<ExerciseSet> sets = new ArrayList<>(); // This is the current list of sets, both existing and new.
    private List<ExerciseSet> deletedSets = new ArrayList<>(); // This is a list of sets to be deleted from the database.

    private IExerciseSetCallback adapterCallback;
    public List<ExerciseSet> getSets() {
        return sets;
    }

    List<ExerciseSet> getDeletedSets() {
        return deletedSets;
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

    public ExerciseSetsAdapter(IExerciseSetCallback callback) {
        adapterCallback = callback;
    }

    private void onBindSetsViewHolder(SetsViewHolder holder, int position) {
        // Modify the view that gives the set's number.
        holder.getSetNumberView().setImageResource(getSetTypeImage(holder.getAdapterPosition()));
        // Create listener for the "more options" button. credit: Shaba Aafreen @https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item
        holder.getSetNumberView().setOnClickListener(view -> {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(adapterCallback.getContext(), holder.getSetNumberView());
            //inflating menu from xml resource
            popup.inflate(R.menu.exercise_set_options_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                int setPos = holder.getAdapterPosition();
                switch (item.getItemId()) {
                    case R.id.menu_exercise_set_warmup:
                        changeTypeForSetAtPosition(setPos, ExerciseSetType.WARMUP);
                        break;
                    case R.id.menu_exercise_set_regular:
                        changeTypeForSetAtPosition(setPos, ExerciseSetType.REGULAR);
                        break;
                    case R.id.menu_exercise_set_tofailure:
                        changeTypeForSetAtPosition(setPos, ExerciseSetType.TOFAILURE);
                        break;
                    default:
                        break;
                }

                holder.getSetNumberView().setImageResource(getSetTypeImage(setPos));
                return true;
            });
            //displaying the popup
            popup.show();
        });

        // Modify the view for deleting the set.
        holder.getDeleteSetView().setOnClickListener(v -> deleteSet(holder.getAdapterPosition()));
    }

    private int getSetTypeImage(int pos) {
        switch (sets.get(pos).getType()) {
            case ExerciseSetType.WARMUP:
                return R.drawable.ic_airline_seat_legroom_reduced_black_24dp;
            case ExerciseSetType.REGULAR:
                return R.drawable.ic_airline_seat_legroom_normal_black_24dp;
            case ExerciseSetType.TOFAILURE:
                return R.drawable.ic_airline_seat_legroom_extra_black_24dp;
            default:
                return R.drawable.ic_airline_seat_legroom_normal_black_24dp;
        }
    }

    private void changeTypeForSetAtPosition(int pos, int newValue) {
        sets.get(pos).setType(newValue);
    }

    interface IExerciseSetCallback {
        Context getContext();
    }

    private void addSet() {
        int positionToAdd = sets.size();
        ExerciseSet setToAdd = new ExerciseSet();
        sets.add(setToAdd);
        notifyItemInserted(positionToAdd);
    }

    private void deleteSet(int position) {
        ExerciseSet setToDelete = sets.get(position);
        sets.remove(position);
        if (setToDelete.getIdExerciseSet() != null)
            deletedSets.add(setToDelete);

        notifyItemRemoved(position);
    }

    private class SetsViewHolder extends RecyclerView.ViewHolder {
        private ImageButton setNumberView;

        private ImageButton deleteSetView;

        SetsViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumberView = itemView.findViewById(R.id.txt_setNumber);
            deleteSetView = itemView.findViewById(R.id.btn_remove);
        }

        ImageButton getDeleteSetView() {
            return deleteSetView;
        }

        ImageButton getSetNumberView() {
            return setNumberView;
        }
    }
}
