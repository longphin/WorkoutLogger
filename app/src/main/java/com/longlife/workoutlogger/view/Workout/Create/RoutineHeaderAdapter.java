/*
 * Created by Longphi Nguyen on 1/26/19 7:37 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/26/19 7:37 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class RoutineHeaderAdapter extends RecyclerTabLayout.Adapter<RecyclerView.ViewHolder> {
    private List<headerObject> data = new ArrayList<>();

    public RoutineHeaderAdapter(ViewPager viewPager) {
        super(viewPager);
    }

    public void addHeader(headerObject header) {
        data.add(header);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new headerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_create_selected_routine, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof headerViewHolder) {
            onBindHeaderViewHolder((headerViewHolder) holder, position);
        }
    }

    private void onBindHeaderViewHolder(headerViewHolder holder, int position) {
        holder.setName(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class headerObject {
        private String name;
        private Long idRoutine;

        public headerObject(Long idRoutine, String name) {
            this.name = name;
            this.idRoutine = idRoutine;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getIdRoutine() {
            return idRoutine;
        }

        public void setIdRoutine(Long idRoutine) {
            this.idRoutine = idRoutine;
        }
    }

    private class headerViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;

        headerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.txt_exerciseName);
        }

        public void setName(String name) {
            nameView.setText(name);
        }
    }
}
