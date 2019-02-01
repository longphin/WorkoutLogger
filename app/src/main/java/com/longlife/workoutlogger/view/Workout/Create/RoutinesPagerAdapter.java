/*
 * Created by Longphi Nguyen on 1/27/19 8:44 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/27/19 8:44 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class RoutinesPagerAdapter extends FragmentStatePagerAdapter {
    private List<Long> idRoutines = new ArrayList<>();

    public RoutinesPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addRoutine(Long idRoutine) {
        idRoutines.add(idRoutine);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return RoutineFragment.newInstance(idRoutines.get(position));
    }

    @Override
    public int getCount() {
        return idRoutines.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "test" + String.valueOf(idRoutines.get(position));
    }
}
