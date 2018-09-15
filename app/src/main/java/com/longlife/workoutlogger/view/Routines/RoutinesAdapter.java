package com.longlife.workoutlogger.view.Routines;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine;

import java.util.ArrayList;
import java.util.List;

public class RoutinesAdapter
        extends RecyclerView.Adapter<RoutinesViewHolder> {

    private static final String TAG = RoutinesAdapter.class.getSimpleName();
    private List<Routine> routines = new ArrayList<>();
    private IClickRoutine onClickCallback;

    public RoutinesAdapter(IClickRoutine onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    @Override
    public RoutinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);

        return (new RoutinesViewHolder(v));
    }


    @Override
    public void onBindViewHolder(RoutinesViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        Routine routine = routines.get(position);

        holder.setNameText(routine.getName() + " (" + String.valueOf(routine.getIdRoutine()) + ")");
        holder.setDescripText(routine.getNote());

        holder.getNameTextView().setOnClickListener(view ->
        {
            onClickCallback.routineClicked(routine.getIdRoutine());
        });
    }

    @Override
    public int getItemCount() {
        if (routines == null)
            return (0);
        return (routines.size());
    }


    public void setRoutines(List<Routine> routines) {
        if (routines == null)
            return;

        this.routines = routines;
        notifyDataSetChanged();
    }

    public void addRoutine(Routine routine) {
        Log.d(TAG, "inserted routine " + routine.getName() + " " + String.valueOf(routine.getIdRoutine()));

        this.routines.add(routine);
        notifyItemInserted(routines.size() - 1);
    }

    public Routine getRoutine(int position) {
        return routines.get(position);
    }

    public void removeRoutine(int position) {
        routines.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreRoutine(Routine restoredItem, int restoredPosition) {
        routines.add(restoredPosition, restoredItem);
        notifyItemInserted(restoredPosition);
    }

    // Interface for when an item is clicked. Should be implemented by the Activity/Fragment to start an edit fragment.
    public interface IClickRoutine {
        // When an exercise is clicked, send the clicked exercise.
        void routineClicked(Long idRoutine);
    }
}

