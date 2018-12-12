/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RoutineCreateAddSetViewHolder
        extends RecyclerView.ViewHolder {
    private View mView; // entire view

    public RoutineCreateAddSetViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }


    public View getView() {
        return mView;
    }
}
