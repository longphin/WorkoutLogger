/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/28/18 6:53 PM.
 */

package com.longlife.workoutlogger.view.Routines;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.R;

public class RoutinesViewHolder
        extends RecyclerViewHolderSwipeable {
    private TextView name;
    private TextView descrip;
    private RelativeLayout background;
    private ConstraintLayout foreground;

    RoutinesViewHolder(View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.txt_routineName);
        this.descrip = itemView.findViewById(R.id.txt_routineDescrip);
        this.background = itemView.findViewById(R.id.background_routine_item);
        this.foreground = itemView.findViewById(R.id.foreground_routine_item);
    }


    @Override
    public RelativeLayout getViewBackground() {
        return background;
    }

    @Override
    public ConstraintLayout getViewForeground() {
        return foreground;
    }


    TextView getNameTextView() {
        return name;
    }


    void setNameText(String s) {
        this.name.setText(s);
    }

    void setDescripText(String s) {
        this.descrip.setText(s);
    }
}

