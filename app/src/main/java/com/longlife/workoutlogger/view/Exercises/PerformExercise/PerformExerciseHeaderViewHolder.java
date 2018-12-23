/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/28/18 6:52 PM.
 */

package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PerformExerciseHeaderViewHolder
        extends RecyclerViewHolderSwipeable {
    private RelativeLayout viewBackground;
    private ConstraintLayout viewForeground;
    private TextView nameText;

    public PerformExerciseHeaderViewHolder(View itemView) {
        super(itemView);

        this.viewBackground = itemView.findViewById(R.id.background_routine_create_exercise);
        this.viewForeground = itemView.findViewById(R.id.foreground_routine_create_exercise);
        this.nameText = itemView.findViewById(R.id.txt_routinecreate_exerciseName);
    }

    public TextView getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText.setText(nameText);
    }

    @Override
    public RelativeLayout getViewBackground() {
        return viewBackground;
    }

    @Override
    public ConstraintLayout getViewForeground() {
        return viewForeground;
    }
}
