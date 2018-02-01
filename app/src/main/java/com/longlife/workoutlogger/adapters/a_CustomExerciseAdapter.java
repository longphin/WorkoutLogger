package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.DataAccessorInterface;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.z_DataAccessor;

import java.util.List;

/**
 * Created by Longphi on 1/30/2018.
 */

public class a_CustomExerciseAdapter extends RecyclerView.Adapter<a_CustomExerciseAdapter.CustomViewHolder> {//6
    private Context context;
    private List<SessionExercise> sessionExercises;
    //private HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash; // <idSessionExercise, List<SessionExerciseSet>>
    private DataAccessorInterface dataSource;

    public a_CustomExerciseAdapter(Context context, RoutineSession routineSession) {
        this.context = context;
        this.dataSource = z_DataAccessor.getInstance();
        this.sessionExercises = this.dataSource.getSessionExercises(routineSession);
        //this.sessionExerciseSetHash = this.dataSource.getSessionExerciseSetHash();
    }

    @Override
    public a_CustomExerciseAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_exercise_list_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(a_CustomExerciseAdapter.CustomViewHolder holder, int position) {
        SessionExercise currentItem = sessionExercises.get(holder.getAdapterPosition());
        currentItem.setDisplayOrder(holder.getAdapterPosition());

        Exercise currentExercise = dataSource.getExerciseFromSession(currentItem);
        holder.name.setText(currentExercise.getName());
        holder.description.setText(currentExercise.getDescription());
    }

    @Override
    public int getItemCount() {
        if (sessionExercises == null) return (0);
        return sessionExercises.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;
        private ViewGroup container;
        private TextView name;
        private TextView description;
        private Button editExercise;

        public CustomViewHolder(View itemView) {
            super(itemView);

            this.container = (ViewGroup) itemView.findViewById(R.id.root_exerciseListItem);
            this.name = (TextView) itemView.findViewById(R.id.text_exerciseListItem_name);
            this.description = (TextView) itemView.findViewById(R.id.text_exerciseListItem_description);
            this.editExercise = (Button) itemView.findViewById(R.id.button_editExercise);
        }
    }
}
