package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;

import java.util.List;

/**
 * Created by Longphi on 1/30/2018.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.CustomViewHolder> {
    private Context context;
    private ExerciseSetListAdapter setAdapter;
    private List<SessionExercise> sessionExercises;
    //private HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash; // <idSessionExercise, List<SessionExerciseSet>>
    private DataAccessorInterface dataSource;
    private EditText focusedEditText;

    public ExerciseListAdapter(Context context, RoutineSession routineSession) {
        this.context = context;
        this.dataSource = DataAccessor.getInstance();
        this.sessionExercises = this.dataSource.getSessionExercises(routineSession);
        //this.sessionExerciseSetHash = this.dataSource.getSessionExerciseSetHash();
    }

    @Override
    public ExerciseListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_exercise_list_item, parent, false);
        return (new CustomViewHolder(v));
    }

    @Override
    public void onBindViewHolder(ExerciseListAdapter.CustomViewHolder holder, int position) {
        SessionExercise currentItem = sessionExercises.get(holder.getAdapterPosition());
        currentItem.setDisplayOrder(holder.getAdapterPosition());

        Exercise currentExercise = dataSource.getExerciseFromSession(currentItem);
        setAdapter = new ExerciseSetListAdapter(context, dataSource.getSessionExerciseSets(currentItem), dataSource.getExerciseFromSession(currentItem));
        holder.name.setText(currentExercise.getName());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(setAdapter);
    }

    @Override
    public int getItemCount() {
        if (sessionExercises == null) return (0);
        return sessionExercises.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private final View.OnClickListener exerciseItemOnClickListener = new ExerciseItemOnClickListener();
        public RecyclerView recyclerView;
        public ViewGroup container;
        private TextView name;

        public CustomViewHolder(View itemView) {
            super(itemView);

            this.container = itemView.findViewById(R.id.root_exerciseList_item);
            this.recyclerView = itemView.findViewById(R.id.parentRecycler);
            this.name = itemView.findViewById(R.id.parentTitle);

            this.container.setOnClickListener(exerciseItemOnClickListener);
        }

        // OnClick listener for an ExerciseItem
        private class ExerciseItemOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildAdapterPosition(view);

                Toast.makeText(context,
                        "exercises #: " + String.valueOf(sessionExercises.get(itemPosition).getIdSessionExercise()),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
