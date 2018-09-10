package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.List;

public class PerformExerciseAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    Context context;
    private List<RoutineExerciseHelper> exercisesToInclude;

    public PerformExerciseAdapter(List<RoutineExerciseHelper> exercisesToInclude) {
        this.exercisesToInclude = exercisesToInclude;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case HEADER_TYPE:
                v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
                return new PerformExerciseHeaderViewHolder(v);
            default:
                v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
                return new PerformExerciseHeaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();

        if (holder instanceof PerformExerciseHeaderViewHolder) {
            bindHeaderViewHolder((PerformExerciseHeaderViewHolder) holder, position);
            return;
        }
    }

    private void bindHeaderViewHolder(PerformExerciseHeaderViewHolder holder, int position) {
        final int headerIndex = getHeaderIndex(position);
        final RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);

        if (headerItem.IsExpanded()) {
            // [TODO] change arrow to point up
        } else {
            // change arrow to point down.
        }

        final Exercise exercise = headerItem.getExercise();

        holder.setNameText(exercise.getName() + " (" + String.valueOf(exercise.getIdExercise()) + " -> " + String.valueOf(exercise.getCurrentIdExerciseHistory()) + ")");
    }


    // Get the header position given the recyclerview position.
    // Since some sub items take up adapter positions, this needs to iterate to see if headers are expanded.
    public int getHeaderIndex(int position) {
        int count = 0; // Keeps track of the iterator count for visible items.
        int headerCount = 0; // Keeps track of the header position.

        for (RoutineExerciseHelper reh : exercisesToInclude) {
            if (count >= position)
                return headerCount;

            if (reh.IsExpanded()) {

                count += reh.getSets().size() + 1; // The +1 is for the "add set" button view.
                if (count >= position)
                    return headerCount;
            }
            count += 1; // go to next header

            headerCount += 1;
        }

        throw new IndexOutOfBoundsException("Header not found for " + String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return exercisesToInclude.size();
    }
}
