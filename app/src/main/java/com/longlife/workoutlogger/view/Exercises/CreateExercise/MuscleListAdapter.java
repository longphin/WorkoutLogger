package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.MuscleClass;
import com.longlife.workoutlogger.enums.MuscleGroupWithMuscles;

import java.util.List;

public class MuscleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private static final Map<MuscleGroup, List<Muscle>> data = MuscleGroup.getMuscleGroups();
    private static final int HEADER_TYPE = 0;
    private static final int SET_TYPE = 1;
    private static final List<MuscleClass> data = MuscleGroupWithMuscles.getMusclesWithGroupHeaders();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case HEADER_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_muscle_group, parent, false);
                return new MuscleGroupListViewHolder(v);
            case SET_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selectable_muscle, parent, false);
                return new MuscleListViewHolder(v);

            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_muscle_group, parent, false);
                return new MuscleGroupListViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();

        if (holder instanceof MuscleGroupListViewHolder) {
            bindHeaderViewHolder((MuscleGroupListViewHolder) holder, position);
            return;
        }
        if (holder instanceof MuscleListViewHolder) {
            bindSetViewHolder((MuscleListViewHolder) holder, position);
            return;
        }
    }

    private void bindHeaderViewHolder(MuscleGroupListViewHolder holder, int position) {
        holder.setName(data.get(position).getName());
    }

    private void bindSetViewHolder(MuscleListViewHolder holder, int position) {
        holder.setName(data.get(position).getName());
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isHeaderItem() ? HEADER_TYPE : SET_TYPE;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
