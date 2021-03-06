/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/28/18 6:52 PM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class ExercisesWithSetsViewHolder extends RecyclerViewHolderSwipeable {
    private RelativeLayout viewBackground;
    private ConstraintLayout viewForeground;
    private ImageView upButton;
    private ImageView downButton;
    private TextView restTextView;

    public ExercisesWithSetsViewHolder(View itemView) {
        super(itemView);

        this.viewBackground = itemView.findViewById(R.id.item_routine_create_exercise_set_background);
        this.viewForeground = itemView.findViewById(R.id.item_routine_create_exercise_set_foreground);
        this.restTextView = itemView.findViewById(R.id.txt_routine_create_exercise_set_rest);
    }

    @Override
    public RelativeLayout getViewBackground() {
        return viewBackground;
    }

    @Override
    public ConstraintLayout getViewForeground() {
        return viewForeground;
    }


    public TextView getRestTextView() {
        return restTextView;
    }

    public View getView() {
        return viewForeground;
    }
}
