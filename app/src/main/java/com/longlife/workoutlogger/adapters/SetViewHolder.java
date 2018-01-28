package com.longlife.workoutlogger.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.longlife.workoutlogger.R;

/**
 * Created by Longphi on 1/26/2018.
 */

public class SetViewHolder extends RecyclerView.ViewHolder {
    public EditText weights;
    public EditText reps;

    public SetViewHolder(View itemView) {
        super(itemView);
        this.weights = (EditText) itemView.findViewById(R.id.editText_exerciseSet_weight);
        this.reps = (EditText) itemView.findViewById(R.id.editText_exerciseSet_rep);
    }
}
