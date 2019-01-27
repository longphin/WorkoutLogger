/*
 * Created by Longphi Nguyen on 1/26/19 3:12 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/26/19 3:12 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

public class SelectedExercisesAdapter extends PagerAdapter {
    private List<exerciseItemInRoutine> data = new ArrayList<>();

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_workout_create_selected_routine, container, false);

        TextView nameView = view.findViewById(R.id.workout_create_selected_routine_name);
        nameView.setText(data.get(position).getName());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getName();
    }

    void addExercise(Long idExercise, String exerciseName) {
        data.add(new exerciseItemInRoutine(idExercise, exerciseName, 1));
        notifyDataSetChanged();
    }

    private class exerciseItemInRoutine {
        Long idRoutine;

        String name;

        exerciseItemInRoutine(Long idRoutine, String name, int numberOfSets) {
            this.idRoutine = idRoutine;
            this.name = name;
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
}
