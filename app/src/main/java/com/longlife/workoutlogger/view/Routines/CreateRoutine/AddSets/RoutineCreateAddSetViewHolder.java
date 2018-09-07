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
