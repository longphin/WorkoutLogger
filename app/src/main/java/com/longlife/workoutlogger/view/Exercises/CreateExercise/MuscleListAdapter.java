package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.MuscleGroupWithMuscles;

import java.util.List;

public class MuscleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private static final Map<MuscleGroup, List<Muscle>> data = MuscleGroup.getMuscleGroups();
    private static final int HEADER_TYPE = 0;
    private static final int SET_TYPE = 1;
    // Number of columns for the recyclerview to show.
    public static final int NUMBER_OF_COLUMNS = 2; // [TODO] This should be based on device's screen size, not a static number.
    private static final int FOOTER_PADDING_TYPE = 2;
    private static final int HEADER_PADDING_TYPE = 3;
    //private List<MuscleClass> data = MuscleGroupWithMuscles.getMusclesWithGroupHeaders();
    private List<MuscleListHelper> data = MuscleGroupWithMuscles.getAllMuscleGroups();
    private int itemCount;

    public MuscleListAdapter() {
        super();

        updateHeaderPositions();
    }

    private void updateHeaderPositions() {
        /*MuscleListHelper lastItem = data.get(data.size()-1);
        return lastItem.getVisiblePosition()
                + (lastItem.isExpanded() ? lastItem.getMuscles().size() + lastItem.getFooterPadding() : 0);*/
        int pos = 0;
        for (MuscleListHelper item : data) {
            item.setVisiblePosition(pos);
            if (item.isExpanded()) pos += item.getMuscles().size() + item.getFooterPadding();

            pos += 1 + item.getHeaderPadding();
            itemCount = pos;
        }
    }

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
            case FOOTER_PADDING_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_padding, parent, false);
                return new MusclePaddingListViewHolder(v);
            case HEADER_PADDING_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_muscle_group_padding, parent, false);
                return new MusclePaddingListViewHolder(v);
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
        if (holder instanceof MusclePaddingListViewHolder) {
            bindPaddingViewHolder((MusclePaddingListViewHolder) holder, position);
            return;
        }
    }

    private void bindHeaderViewHolder(MuscleGroupListViewHolder holder, int position) {
        holder.setName(getNameAtPosition(position));
    }

    private void bindSetViewHolder(MuscleListViewHolder holder, int position) {
        holder.setName(getNameAtPosition(position));
    }

    private void bindPaddingViewHolder(MusclePaddingListViewHolder holder, int position) {
    }

    private String getNameAtPosition(int position) {
        for (MuscleListHelper item : data) {
            int headerPosition = item.getVisiblePosition();
            int headerPadding = item.getHeaderPadding();

            if (headerPosition == position) return item.getMuscleGroupName();
            if (item.isExpanded() && position > headerPosition && position <= headerPosition + headerPadding + item.getMuscles().size()) {
                return item.getMuscles().get(position - headerPosition - headerPadding - 1).getName();
            }
        }

        return "Could not find name.";
    }

    @Override
    public int getItemViewType(int position) {
        for (MuscleListHelper item : data) {
            int headerPosition = item.getVisiblePosition();
            int headerPadding = item.getHeaderPadding();

            if (headerPosition == position) return HEADER_TYPE;
            if (item.isExpanded() && position > headerPosition) {
                if (position <= headerPosition + headerPadding) return HEADER_PADDING_TYPE;
                if (position <= headerPosition + headerPadding + item.getMuscles().size())
                    return SET_TYPE;
                if (position <= headerPosition + headerPadding + item.getMuscles().size() + item.getFooterPadding())
                    return FOOTER_PADDING_TYPE;
            }
        }
        return FOOTER_PADDING_TYPE;
    }

    @Override
    public int getItemCount() {
/*        MuscleListHelper lastItem = data.get(data.size()-1);
        return lastItem.getVisiblePosition()
                + (lastItem.isExpanded() ? lastItem.getMuscles().size() + lastItem.getFooterPadding() : 0);*/
        return itemCount;
    }
}
