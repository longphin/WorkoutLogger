package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class RoutineCreateAdapter extends RecyclerView.Adapter<RoutineCreateViewHolder> {
    private List<Exercise> exercisesToInclude = new ArrayList<>();
    /*private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain", "Spanish"
    };*/

    private Context context;

    @Override
    public RoutineCreateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);

        return (new RoutineCreateViewHolder(v));
    }

    @Override
    public void onBindViewHolder(RoutineCreateViewHolder holder, int position) {
        Exercise exercise = exercisesToInclude.get(position);

        StringBuilder sbName = new StringBuilder(100);
        sbName.append(exercise.getName())
                .append(" (")
                .append(exercise.getIdExercise())
                .append(")");

        holder.setNameText(sbName.toString());
        holder.setDescripText(exercise.getDescription());

        /*
        ArrayAdapter<String> exercisesListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        holder.newExerciseBox.setAdapter(exercisesListAdapter);
        */
    }

    @Override
    public int getItemCount() {
        return exercisesToInclude.size();
    }

    public void addExercise(Exercise ex) {
        exercisesToInclude.add(ex);
    }

    public void deleteAtPosition(int pos) {
        try {
            exercisesToInclude.remove(pos);
        } catch (Exception e) {
        }
    }
}
