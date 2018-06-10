package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesViewHolder> {
    private List<Exercise> exercises;
    private ExercisesOverviewViewModel viewModel;

    public ExercisesAdapter(ExercisesOverviewViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercises, parent, false);

        return (new ExercisesViewHolder(v));
    }

    @Override
    public void onBindViewHolder(ExercisesViewHolder holder, int position) {
        Exercise ex = exercises.get(position);

        StringBuilder sbName = new StringBuilder(100);
        sbName.append(ex.getName())
                .append(" (")
                .append(ex.getIdExercise())
                .append(")");

        holder.setNameText(sbName.toString());
        holder.setDescripText(ex.getDescription());

        if (ex.getFavorited()) {
            holder.setFavoriteIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.setFavoriteIcon(R.drawable.ic_favorite_border_black_24dp);
        }

        holder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ex.setFavorited(!ex.getFavorited());
                if (ex.getFavorited()) {
                    holder.setFavoriteIcon(R.drawable.ic_favorite_black_24dp);
                } else {
                    holder.setFavoriteIcon(R.drawable.ic_favorite_border_black_24dp);
                }

                viewModel.updateFavorite(ex.getIdExercise(), ex.getFavorited());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exercises == null) return 0;
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        if (exercises == null) return;

        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public void removeExercise(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreExercise(Exercise ex, int position) {
        exercises.add(position, ex);
        notifyItemInserted(position);
    }
}
