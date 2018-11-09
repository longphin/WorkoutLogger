package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.ExerciseShort;
import com.longlife.workoutlogger.view.Exercises.ExercisesAdapter;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewHolder;

public class ExercisesSelectableAdapter
        extends ExercisesAdapter {
    private final static String TAG = ExercisesSelectableAdapter.class.getSimpleName();
    private IExercisesSelectableAdapterCallback exercisesSelectableCallback;

    public ExercisesSelectableAdapter(Context context, IClickExercise clickExerciseCallback, IExercisesSelectableAdapterCallback selectableAdapterCallback) {
        super(context, clickExerciseCallback);
        this.exercisesSelectableCallback = selectableAdapterCallback;
    }

    @Override
    public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_selectable, parent, false);

        return (new ExercisesViewHolder(v));
    }

    @Override
    protected void bindMyViewHolder(ExercisesViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final ExerciseShort ex = exercises.get(position);

        // Get the checkbox
        CheckBox selectedCheckBox = holder.getSelectedCheckBox();
        if (selectedCheckBox != null) {
            Long idCurrentlySelected = ex.getIdExercise();
            boolean isCurrentlySelected = exercisesSelectableCallback.isIdSelected(idCurrentlySelected);//viewModel.isIdSelected(idCurrentlySelected);
            if (isCurrentlySelected) {
                holder.setSelectedCheckBox(true);
                Log.d(TAG, "Is " + String.valueOf(idCurrentlySelected) + " selected: Yes");
            } else {
                holder.setSelectedCheckBox(false); // This can happen if the holder view was restored (such as when item was deleted, then undone).
                Log.d(TAG, "Is " + String.valueOf(idCurrentlySelected) + " selected: No");
            }

            selectedCheckBox.setOnClickListener(
                    view ->
                    {
                        Long id = ex.getIdExercise();
                        boolean isSelected = exercisesSelectableCallback.isIdSelected(id);
                        if (isSelected) {
                            exercisesSelectableCallback.removeSelectedExcercise(id);
                        } else {
                            exercisesSelectableCallback.addSelectedExercise(id);
                        }
                        Log.d(TAG, String.valueOf(id) + " is " + (!isSelected ? "selected" : "unselected"));
                    }
            );
        }
    }

    public interface IExercisesSelectableAdapterCallback {
        boolean isIdSelected(Long idExercise);

        void removeSelectedExcercise(Long idExercise);

        void addSelectedExercise(Long idExercise);
    }

}
