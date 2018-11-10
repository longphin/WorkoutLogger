package com.longlife.workoutlogger.view.Exercises;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseLocked;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;

import java.util.ArrayList;
import java.util.List;

public abstract class ExercisesListAdapter
        extends RecyclerView.Adapter<ExercisesViewHolder> {
    protected List<ExerciseShort> exercises = new ArrayList<>();
    protected IClickExercise exerciseClickCallback;
    protected Context context;

    public ExercisesListAdapter(Context context, IClickExercise exerciseClickCallback) {
        this.context = context;
        this.exerciseClickCallback = exerciseClickCallback;
    }

    @Override
    public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);

        return (new ExercisesViewHolder(v));
    }

    @Override
    public void onBindViewHolder(ExercisesViewHolder holder, int pos) {
        // Bind common elements.
        bindMyViewHolderCommon(holder, pos);

        // Bind any unique elements.
        bindMyViewHolder(holder, pos);
    }

    private void bindMyViewHolderCommon(ExercisesViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        ExerciseShort ex = exercises.get(position);
        // Name
        holder.setNameText(ex.getName() + " (" + String.valueOf(ex.getIdExercise()) + ")");
        // Description
        holder.setDescripText(ex.getNote());
        // Lock icon
        if (ex.isLocked()) {
            holder.setLockedIcon(R.drawable.ic_lock_black_24dp);
        } else {
            holder.setLockedIcon(R.drawable.ic_lock_open_black_24dp);
        }

        holder.getLockedIcon().setOnClickListener(view ->
                {
                    final boolean newLockStatus = !ex.isLocked();
                    exerciseClickCallback.exerciseLocked(ex.getIdExercise(), newLockStatus);

                    if (newLockStatus) {
                        holder.setLockedIcon(R.drawable.ic_lock_black_24dp);
                    } else {
                        holder.setLockedIcon(R.drawable.ic_lock_open_black_24dp);
                    }
                }
        );

        // Edit exercise
        holder.getNameTextView()
                .setOnClickListener(view ->
                {
                    exerciseClickCallback.exerciseClicked(ex.getIdExercise());
                });

        // Create listener for the "more options" button. credit: Shaba Aafreen @https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item
        if (holder.getMoreOptionsView() != null) {
            holder.getMoreOptionsView().setOnClickListener(view -> {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.getMoreOptionsView());
                //inflating menu from xml resource
                popup.inflate(R.menu.exercise_options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_exercise_options:
                            //handle menu1 click
                            exerciseClickCallback.exercisePerform(ex.getIdExercise(), ex.getName());
                            return true;
                        default:
                            return false;
                    }
                });
                //displaying the popup
                popup.show();
            });
        }
    }

    protected abstract void bindMyViewHolder(ExercisesViewHolder holder, int pos);

    @Override
    public int getItemCount() {
        return exercises.size();
    }


    public void setExercises(List<ExerciseShort> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public void exerciseUpdated(ExerciseShort updatedExercise) {
        final Long idExerciseEdited = updatedExercise.getIdExercise();
        // Find where in the adapter this exercise is and notify the change.
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getIdExercise().equals(idExerciseEdited)) {
                exercises.set(i, updatedExercise);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void exerciseLocked(ExerciseLocked exerciseLocked) {
        final Long idExercise = exerciseLocked.getIdExercise();
        final boolean lockStatus = exerciseLocked.isLocked();

        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getIdExercise().equals(idExercise)) {
                exercises.get(i).setLocked(lockStatus);
                return;
            }
        }
    }

    public ExerciseShort getExercise(int position) {
        return exercises.get(position);
    }

    public void removeExercise(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreExercise(ExerciseShort restoredItem, int restoredPosition) {
        exercises.add(restoredPosition, restoredItem);
        notifyItemInserted(restoredPosition);
    }

    public void addExercise(ExerciseShort ex) {
        exercises.add(ex);
        notifyItemInserted(exercises.size() - 1);
    }

    public int getSwipeDirs(int adapterPosition) {
        if (exercises.get(adapterPosition).isLocked())
            return 0;
        else
            return ItemTouchHelper.RIGHT;
    }

    // Interface for when an item is clicked. Should be implemented by the Activity/Fragment to start an edit fragment.
    public interface IClickExercise {
        // When an exercise is clicked, send the clicked exercise.
        void exerciseClicked(Long idExercise);

        void exerciseLocked(Long idExercise, boolean lockStatus);

        void exercisePerform(Long idExercise, String exerciseName);
    }
}
