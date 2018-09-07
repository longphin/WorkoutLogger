package com.longlife.workoutlogger.view.Exercises;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.view.Exercises.Helper.ExerciseLocked;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter
        extends RecyclerView.Adapter<ExercisesViewHolder> {

    private static final String TAG = ExercisesAdapter.class.getSimpleName();
    private static int lockedIconEnabled = R.drawable.ic_lock_black_24dp;
    private static int lockedIconDisabled = R.drawable.ic_lock_open_black_24dp;
    protected List<Exercise> exercises = new ArrayList<>();
    private IClickExercise exerciseClickCallback;
    private Context context;

    public ExercisesAdapter(Context context, IClickExercise exerciseClickCallback) {
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
        final int position = holder.getAdapterPosition();
        Exercise ex = exercises.get(position);
        // Name
        holder.setNameText(ex.getName() + " (" + String.valueOf(ex.getIdExercise()) + " -> " + String.valueOf(ex.getCurrentIdExerciseHistory()) + ")");
        // Description
        holder.setDescripText(ex.getNote());
        // Lock icon
        if (ex.getLocked()) {
            holder.setLockedIcon(ExercisesAdapter.lockedIconEnabled);
        } else {
            holder.setLockedIcon(ExercisesAdapter.lockedIconDisabled);
        }

        holder.getLockedIcon().setOnClickListener(view ->
                {
/*				ex.setLocked(!ex.getLocked());
				if(ex.getLocked()){
					holder.setLockedIcon(R.drawable.ic_favorite_black_24dp);
				}else{
					holder.setLockedIcon(R.drawable.ic_favorite_border_black_24dp);
				}*/

                    //viewModel.updateLockedStatus(ex.getIdExercise(), ex.getLocked());
                    final boolean newLockStatus = !ex.getLocked();
                    exerciseClickCallback.exerciseLocked(ex.getIdExercise(), newLockStatus);

                    if (newLockStatus) {
                        holder.setLockedIcon(ExercisesAdapter.lockedIconEnabled);
                    } else {
                        holder.setLockedIcon(ExercisesAdapter.lockedIconDisabled);
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

    @Override
    public int getItemCount() {
        return exercises.size();
    }


    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public void exerciseUpdated(Exercise updatedExercise) {
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
                //notifyItemChanged(i);
                return;
            }
        }
    }

    public Exercise getExercise(int position) {
        return exercises.get(position);
    }

    public void removeExercise(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreExercise(Exercise restoredItem, int restoredPosition) {
        exercises.add(restoredPosition, restoredItem);
        notifyItemInserted(restoredPosition);
    }

    public void addExercise(Exercise ex) {
        exercises.add(ex);
        notifyItemInserted(exercises.size() - 1);
    }

    public int getSwipeDirs(int adapterPosition) {
        if (exercises.get(adapterPosition).getLocked())
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
