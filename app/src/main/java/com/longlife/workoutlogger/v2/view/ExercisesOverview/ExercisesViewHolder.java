package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class ExercisesViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView descrip;

    public ExercisesViewHolder(View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.txt_exerciseName);
        this.descrip = itemView.findViewById(R.id.txt_exerciseDescrip);
    }

    public void setNameText(String s) {
        this.name.setText(s);
    }

    public void setDescripText(String s) {
        this.descrip.setText(s);
    }
}
