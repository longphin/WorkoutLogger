package com.longlife.workoutlogger.view.Exercises;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExercisesListRemakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int EXERCISE_TYPE = 1;
    private List<IViewItem> data = new ArrayList<>();
    private Set<String> headers = new HashSet<>();

    public ExercisesListRemakeAdapter(List<ExerciseShort> exercises) {
        //Set<String> headers = new HashSet<>(); // A temporary set to keep track of which headers to create.
        for (int i = 0; i < exercises.size(); i++) // [TODO] Make this an async task.
        {
            ExerciseShort ex = exercises.get(i);
            // Get how the exercise will be grouped by.
            String name = ex.getName();
            String headerCategory = name.substring(0, Math.min(1, name.length()));
            headers.add(headerCategory);//new headerItem(headerCategory, headerCategory));
            data.add(new exerciseItem(headerCategory, i, ex));
        }
        // Include headers
        for (String s : headers) {
            data.add(new headerItem(s, s));
        }

        // Sort data.
        Collections.sort(data);
    }

    // When inserting an exercise, split the data into two to see which chunk the new exercise should be inserted.
    public void addExercise(ExerciseShort exerciseShort) { // [TODO] not yet correct.
        String name = exerciseShort.getName();
        String category = name.substring(0, Math.min(1, name.length()));
        IViewItem itemToAdd = new exerciseItem(category, 0, exerciseShort);

        //Set<String> headers = new HashSet<>(); // A temporary set to keep track of which headers to create.
        int lowerbound = 0;
        int center = (int) Math.floor(data.size() / 2);
        int upperbound = data.size() - 1;
        while (center - lowerbound > 0 || upperbound - center > 0) {
            IViewItem centerItem = data.get(center);
            //if (category.compareTo(centerItem.category()) < 0 && name.compareTo(centerItem.toString()) < 0) // The item belongs in the lower section.
            if (itemToAdd.compareTo(centerItem) < 0) {
                upperbound = center;
            } else { // The item belongs in the upper section.
                lowerbound = center + 1;
            }
            center = (int) Math.floor((upperbound + lowerbound) / 2);
        }

        IViewItem lowerItem = data.get(lowerbound);
        IViewItem upperItem = data.get(upperbound);
        //if(category.compareTo(lowerItem.category()) <= 0 && name.compareTo(lowerItem.toString()) < 0) // The item is the lowest item.
        if (itemToAdd.compareTo(lowerItem) < 0) {
            if (!headers.contains(category)) {
                headers.add(category);
                data.add(lowerbound, new headerItem(category, category));
                data.add(lowerbound + 1, new exerciseItem(category, lowerbound, exerciseShort));
                notifyItemRangeInserted(lowerbound, 2);
            } else {
                data.add(lowerbound, new exerciseItem(category, lowerbound, exerciseShort));
                notifyItemInserted(lowerbound);
            }
            //} else if (category.compareTo(upperItem.category()) >= 0 && name.compareTo(upperItem.toString()) > 0){ // THe item is the highest item.
        } else if (itemToAdd.compareTo(upperItem) > 0) {
            if (!headers.contains(category)) {
                headers.add(category);
                data.add(upperbound + 1, new headerItem(category, category));
                data.add(upperbound + 2, new exerciseItem(category, upperbound + 2, exerciseShort));
                notifyItemRangeInserted(upperbound + 1, 2);
            } else {
                data.add(upperbound + 1, new exerciseItem(category, upperbound + 1, exerciseShort));
                notifyItemInserted(upperbound + 1);
            }
        } else { // The item is the middle.
            if (!headers.contains(category)) {
                headers.add(category);
                data.add(center, new headerItem(category, category));
                data.add(center + 1, new exerciseItem(category, center + 1, exerciseShort));
                notifyItemRangeInserted(center, 2);
            } else {
                data.add(center, new exerciseItem(category, center, exerciseShort));
                notifyItemInserted(center);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EXERCISE_TYPE) return exerciseViewHolder(parent);
        return headerViewHolder(parent);
    }

    private RecyclerView.ViewHolder exerciseViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false); // [TODO] create another layout
        return new ExerciseListExerciseViewHolder(v);
    }

    private RecyclerView.ViewHolder headerViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercises_header, parent, false); // [TODO] create another layout
        return new ExerciseListHeaderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExerciseListHeaderViewHolder) {
            onBindHeaderViewHolder((ExerciseListHeaderViewHolder) holder, position);
            return;
        }

        if (holder instanceof ExerciseListExerciseViewHolder) {
            onBindExerciseViewHolder((ExerciseListExerciseViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).itemType();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void onBindHeaderViewHolder(ExerciseListHeaderViewHolder holder, int position) {
        if (data.get(position) instanceof headerItem) {
            holder.getNameTextView().setText((data.get(position)).toString());
        }
    }

    private void onBindExerciseViewHolder(ExerciseListExerciseViewHolder holder, int position) {
        if (data.get(position) instanceof exerciseItem) {
            holder.getNameTextView().setText((data.get(position)).toString());
        }
    }

    private abstract class IViewItem implements Comparable<IViewItem> {
        @Override
        public int compareTo(@NonNull IViewItem other) {
            // Order by group.
            int categoryComparison = this.category().compareTo(other.category());
            if (categoryComparison != 0) return categoryComparison;

            if (this.itemType() > other.itemType()) // Order by header then sub item.
                return 1;
            else if (this.itemType() < other.itemType())
                return -1;
            else // Else, the items are of the same type, so we compare the names.
                switch (this.toString().compareTo(other.toString())) {
                    case -1:
                        return -1;
                    case 1:
                        return 1;
                    default:
                        return this.id().compareTo(other.id()); // Else, if they have the same name, then compare the ids.
                }
        }

        abstract String category();

        abstract int itemType();

        abstract Long id();
    }

    private class headerItem extends IViewItem {
        private String name;
        private String category;

        headerItem(String category, String name) {
            this.category = category;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        String category() {
            return category;
        }

        @Override
        Long id() {
            return Long.valueOf(itemType());
        }

        @Override
        int itemType() {
            return HEADER_TYPE;
        }
    }

    private class exerciseItem extends IViewItem {
        private String category;
        private ExerciseShort exercise;
        private int headerIndex;

        exerciseItem(String category, int headerIndex, ExerciseShort exercise) {
            this.exercise = exercise;
            this.headerIndex = headerIndex;
            this.category = category;
        }

        @Override
        public String toString() {
            return exercise.getName();
        }


        @Override
        String category() {
            return category;
        }

        @Override
        Long id() {
            return exercise.getIdExercise();
        }

        @Override
        int itemType() {
            return EXERCISE_TYPE;
        }
    }
}
