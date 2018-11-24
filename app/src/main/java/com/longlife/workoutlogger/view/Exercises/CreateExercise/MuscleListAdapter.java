package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.Muscle;
import com.longlife.workoutlogger.model.ExerciseMuscle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MuscleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int SET_TYPE = 1;
    // Number of columns for the recyclerview to show.
    public static final int NUMBER_OF_COLUMNS = 2; // [TODO] This should be based on device's screen size, not a static number.
    private static final int FOOTER_PADDING_TYPE = 2;
    private static final int HEADER_PADDING_TYPE = 3;
    private static final String TAG = MuscleListAdapter.class.getSimpleName();
    private int itemCount;

    private List<MuscleListHelper> data;
    private Set<Integer> selectedIdMuscle = new HashSet<>(); // List of idMuscle for each muscle that is selected to be a part of the exercise.

    private void updateHeaderPositions() {
        int pos = 0;
        for (MuscleListHelper item : data) {
            item.setVisiblePosition(pos);
            if (item.isExpanded()) pos += item.getMuscles().size() + item.getFooterPadding();

            pos += 1 + item.getHeaderPadding();
            itemCount = pos;
        }
    }

    // Set a list of muscles as selected.
    public void setDataAsSelected(Set<ExerciseMuscle> selectedMuscles) {
        for (MuscleListHelper muscleListItem : data) {
            for (Muscle muscleItem : muscleListItem.getMuscles()) {
                for (ExerciseMuscle selectedMuscle : selectedMuscles) {
                    if (muscleItem.getIdMuscle() == selectedMuscle.getIdMuscle()) {
                        selectedIdMuscle.add(muscleItem.getIdMuscle());
                        muscleItem.setSelected(true);
                    }
                }
            }
        }
        notifyDataSetChanged();
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

    public MuscleListAdapter(List<MuscleListHelper> data) {
        super();

        this.data = data;
        updateHeaderPositions();
    }

    private void bindHeaderViewHolder(MuscleGroupListViewHolder holder, int position) {
        holder.setName(getNameAtPosition(position));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();

        if (holder instanceof MuscleGroupListViewHolder) {
            bindHeaderViewHolder((MuscleGroupListViewHolder) holder, position);
            return;
        }
        if (holder instanceof MuscleListViewHolder) {
            bindMuscleViewHolder((MuscleListViewHolder) holder, position);
            return;
        }
        if (holder instanceof MusclePaddingListViewHolder) {
            bindPaddingViewHolder((MusclePaddingListViewHolder) holder, position);
            return;
        }
    }

    private void bindMuscleViewHolder(MuscleListViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        String name = getNameAtPosition(pos);
        holder.setName(name);
        boolean isItemSelected = isItemSelected(pos);
        holder.getCheckboxView().setChecked(isItemSelected);
        Integer selected_idMuscle = getMuscleIdForPosition(pos);
        if (isItemSelected) includeMuscle(selected_idMuscle);
        //Log.d(TAG, name + " is " + (isItemSelected ? "currently selected" : "currently not selected"));
        holder.getCheckboxView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPos = holder.getAdapterPosition();
                changeItemSelectedStatus(clickedPos);
            }
        });
    }

    private Integer getMuscleIdForPosition(int position) {
        for (MuscleListHelper item : data) {
            int headerPosition = item.getVisiblePosition();
            int headerPadding = item.getHeaderPadding();

            if (headerPosition == position)
                return null; // We do not change selection status for header items.
            if (item.isExpanded() && position > headerPosition && position <= headerPosition + headerPadding + item.getMuscles().size()) {
                return item.getMuscles().get(position - headerPosition - headerPadding - 1).getIdMuscle();
            }
        }

        return null;
    }

    private void includeMuscle(Integer idMuscle) {
        if (idMuscle != null) {
            selectedIdMuscle.add(idMuscle);
        }
    }

    private void changeItemSelectedStatus(int position) {
        for (MuscleListHelper item : data) {
            int headerPosition = item.getVisiblePosition();
            int headerPadding = item.getHeaderPadding();

            if (headerPosition == position)
                return; // We do not change selection status for header items.
            if (item.isExpanded() && position > headerPosition && position <= headerPosition + headerPadding + item.getMuscles().size()) {
                Muscle muscle = item.getMuscles().get(position - headerPosition - headerPadding - 1);
                boolean isSelected = muscle.isSelected();
                int idMuscle = muscle.getIdMuscle();
                if (isSelected) {
                    // Remove muscle.
                    removeMuscle(idMuscle);
                } else {
                    // Add muscle.
                    includeMuscle(idMuscle);
                }
                muscle.setSelected(!isSelected);
                Log.d(TAG, muscle.getName() + " was " + (isSelected ? "selected" : "unselected"));

                return;
            }
        }
    }

    private boolean isItemSelected(int position) {
        for (MuscleListHelper item : data) {
            int headerPosition = item.getVisiblePosition();
            int headerPadding = item.getHeaderPadding();

            if (headerPosition == position)
                return false; // We do not change selection status for header items.
            if (item.isExpanded() && position > headerPosition && position <= headerPosition + headerPadding + item.getMuscles().size()) {
                return item.getMuscles().get(position - headerPosition - headerPadding - 1).isSelected();
            }
        }

        return false;
    }

    private void removeMuscle(Integer idMuscle) {
        if (idMuscle != null) {
            selectedIdMuscle.remove(idMuscle);
        }
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
        return itemCount;
    }

    public Set<ExerciseMuscle> getExerciseMuscles() {
        Set<ExerciseMuscle> muscles = new HashSet<>();
        for (Integer idMuscle : selectedIdMuscle) {
            muscles.add(new ExerciseMuscle(Long.valueOf(idMuscle)));
        }

        return muscles;
    }
}
