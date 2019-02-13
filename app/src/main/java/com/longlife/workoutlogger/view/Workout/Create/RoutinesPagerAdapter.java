/*
 * Created by Longphi Nguyen on 1/27/19 8:44 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/27/19 8:44 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import com.longlife.workoutlogger.model.Routine;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class RoutinesPagerAdapter extends FragmentStatePagerAdapter {
    private List<routineTabHelper> routineTabs = new ArrayList<>();

    public RoutinesPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addRoutines(List<Routine> routines) {
        for (Routine r : routines) {
            routineTabs.add(new routineTabHelper(r));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return RoutineFragment.newInstance(routineTabs.get(position).getIdRoutine());
    }

    @Override
    public int getCount() {
        return routineTabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String label = routineTabs.get(position).getName();

        return (label == null || label.isEmpty() ? "Unnamed" : label) +
                " " + String.valueOf(routineTabs.get(position).getIdRoutine());
    }

    public void addRoutine(Routine r) {
        routineTabs.add(new routineTabHelper(r));
        notifyDataSetChanged();
    }

    private class routineTabHelper {
        private Long idRoutine;
        private String name;

        routineTabHelper(Routine routine) {
            this.idRoutine = routine.getIdRoutine();
            this.name = routine.getName();
        }

        public Long getIdRoutine() {
            return idRoutine;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
