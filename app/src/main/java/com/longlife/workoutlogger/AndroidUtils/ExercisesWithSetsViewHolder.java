package com.longlife.workoutlogger.AndroidUtils;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

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
