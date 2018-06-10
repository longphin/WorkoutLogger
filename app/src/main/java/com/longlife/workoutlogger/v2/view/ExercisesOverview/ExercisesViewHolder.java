package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class ExercisesViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView descrip;
    private RelativeLayout viewBackground;
    private ConstraintLayout viewForeground;
    public ImageView favoriteIcon;

    public ExercisesViewHolder(View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.txt_exerciseName);
        this.descrip = itemView.findViewById(R.id.txt_exerciseDescrip);
        this.viewBackground = itemView.findViewById(R.id.background_exercise_item);
        this.viewForeground = itemView.findViewById(R.id.foreground_exercise_item);
        this.favoriteIcon = itemView.findViewById(R.id.icon_exercise_favorite);

    }

    public void setFavoriteIcon(int icon) {
        favoriteIcon.setImageResource(icon);
    }

    public void setNameText(String s) {
        this.name.setText(s);
    }

    public void setDescripText(String s) {
        this.descrip.setText(s);
    }

    // When the favorite icon is clicked, mark the favorite status.
    private class FavoriteListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

        }
    }

    public RelativeLayout getViewBackground() {
        return viewBackground;
    }

    public ConstraintLayout getViewForeground() {
        return viewForeground;
    }
}
