/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class MuscleGroupListViewHolder extends RecyclerView.ViewHolder implements MuscleListAdapter.IViewHolder {
    private TextView nameTextView;

    MuscleGroupListViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_muscle_group_name);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }

    @Override
    public void onDestroy() {
        //nameTextView = null;
    }
}
