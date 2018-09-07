package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.List;

public class PerformExerciseAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RoutineExerciseHelper> exercises;

    public PerformExerciseAdapter(List<SessionExerciseSet> sets) {

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
