package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.DataAccessor;
import com.longlife.workoutlogger.model.DataAccessorInterface;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;

import java.util.List;

/**
 * Created by Longphi on 1/30/2018.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.CustomViewHolder> {//6
    private Context context;
    private ExerciseSetListAdapter setAdapter;
    private List<SessionExercise> sessionExercises;
    //private HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash; // <idSessionExercise, List<SessionExerciseSet>>
    private DataAccessorInterface dataSource;

    public ExerciseListAdapter(Context context, RoutineSession routineSession) {
        this.context = context;
        this.dataSource = DataAccessor.getInstance();
        this.sessionExercises = this.dataSource.getSessionExercises(routineSession);
        //this.sessionExerciseSetHash = this.dataSource.getSessionExerciseSetHash();
    }

    @Override
    public ExerciseListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_exercise_list_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExerciseListAdapter.CustomViewHolder holder, int position) {
        SessionExercise currentItem = sessionExercises.get(holder.getAdapterPosition());
        currentItem.setDisplayOrder(holder.getAdapterPosition());

        Exercise currentExercise = dataSource.getExerciseFromSession(currentItem);
        setAdapter = new ExerciseSetListAdapter(context, dataSource.getSessionExerciseSets(currentItem));
        holder.name.setText(currentExercise.getName());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(setAdapter);
        //holder.description.setText(currentExercise.getDescription());
    }

    @Override
    public int getItemCount() {
        if (sessionExercises == null) return (0);
        return sessionExercises.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;
        private TextView name;

        public CustomViewHolder(View itemView) {
            super(itemView);

            this.recyclerView = (RecyclerView) itemView.findViewById(R.id.parentRecycler);
            this.name = (TextView) itemView.findViewById(R.id.parentTitle);
        }
    }
}
