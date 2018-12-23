/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class ExercisesListRemakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int EXERCISE_TYPE = 1;
    private List<IViewItem> data = new ArrayList<>();
    private Set<String> headers = new HashSet<>();
    private List<IExerciseListable> originalData;
    private IClickExercise callback;
    private static final String TAG = ExercisesListRemakeAdapter.class.getSimpleName();

    ExercisesListRemakeAdapter(IClickExercise callback, List<IExerciseListable> exercises) {
        this.callback = callback;
        resetData(exercises);
    }

    void resetData(List<IExerciseListable> exercises) {
        Log.d(TAG, "resetData");
        originalData = exercises;
        setData(exercises);
    }

    public void setData(List<IExerciseListable> exercises) {
        Log.d(TAG, "setData");
        data.clear();
        headers.clear();
        for (int i = 0; i < exercises.size(); i++) // [TODO] Make this an async task.
        {
            IExerciseListable ex = exercises.get(i);
            // Get how the exercise will be grouped by.
            String headerCategory = ex.getCategory();
            headers.add(headerCategory);
            addItem(new exerciseItem(headerCategory, ex, ex.getNote()));
        }
        // Include headers
        for (String s : headers) {
            addItem(new headerItem(s, s));
        }

        // Sort data.
        Collections.sort(data);
        notifyDataSetChanged();
    }

    private void addItem(IViewItem item) {
        data.add(item);
    }

    void restoreExercise(ExerciseShort exerciseToRestore) {
        addExercise(exerciseToRestore);
    }

    private void addItem(int pos, IViewItem item) {
        data.add(pos, item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EXERCISE_TYPE) return exerciseViewHolder(parent);
        return headerViewHolder(parent);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder instanceof IViewHolder) {
            ((IViewHolder) holder).onDestroy();
        }
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

    void exerciseUpdated(ExerciseUpdated updated) {
        Log.d(TAG, "exerciseUpdated");
        Long idExercise = updated.getIdExercise();
        boolean wasCategorizationChanged = false; // Will be true if the item any fields that would change the exercise position is updated.  // [TODO] Does not seem to be working.
        for (int i = 0; i < originalData.size(); i++) {
            IExerciseListable item = originalData.get(i);
            if (item.getIdExercise().equals(idExercise)) {
                if (!wasCategorizationChanged && !item.getName().equals(updated.getName())) {
                    wasCategorizationChanged = true;
                }
                item.update(updated);
            }
        }

        setData(originalData);
        /*
        if(wasCategorizationChanged) // Need to reset the views because categorization was changed.  // [TODO] Because wasCategorization is not correct, logic to only update views when the category for an exercise changes.
        {
        }else{ // else, no items need the order changed, so we only notify what items were updated.
            for(int i=0; i<data.size(); i++)
            {
                IViewItem item = data.get(i);
                if(item instanceof exerciseItem && item.id().equals(idExercise))
                {
                    ((exerciseItem) item).updateNoCategoryChanges(updated);
                    notifyItemChanged(i);
                }
            }
        }
        */
    }

    // When inserting an exercise, split the data into two to see which chunk the new exercise should be inserted.
    void addExercise(ExerciseShort exerciseShort) {
        Log.d(TAG, "addExercise");
        // Add the exercise to the data list.
        originalData.add(exerciseShort);

        // Add the item to the view list.
        //String name = exerciseShort.getName();
        String category = exerciseShort.getCategory();//name.substring(0, Math.min(1, name.length()));
        IViewItem itemToAdd = new exerciseItem(category, exerciseShort, exerciseShort.getNote());

        // If this is the first item in the dataset, then we just insert it the first position.
        if (data.size() == 0) {
            headers.add(category);
            addItem(0, new headerItem(category, category));
            addItem(1, new exerciseItem(category, exerciseShort, exerciseShort.getNote()));
            notifyItemRangeInserted(0, 2);
            return;
        }

        // Determine where to place the new item. Do a binary search, where at each step, it splits the slice into 2 chunks, and then it picks which chunk the new item belongs to.
        int lowerbound = 0;
        int center = (int) Math.floor(data.size() / 2);
        int upperbound = data.size() - 1;

        while (center - lowerbound > 0 || upperbound - center > 0) {
            IViewItem centerItem = data.get(center);

            if (itemToAdd.compareTo(centerItem) < 0) { // The item belongs in the lower section.
                upperbound = center;
            } else { // The item belongs in the upper section.
                lowerbound = center + 1;
            }
            center = (int) Math.floor((upperbound + lowerbound) / 2);
        }

        IViewItem lowerItem = data.get(lowerbound);
        IViewItem upperItem = data.get(upperbound);
        // The item is the lowest item.
        if (itemToAdd.compareTo(lowerItem) < 0) {
            if (!headers.contains(category)) {
                headers.add(category);
                addItem(lowerbound, new headerItem(category, category));
                addItem(lowerbound + 1, new exerciseItem(category, exerciseShort, exerciseShort.note));
                notifyItemRangeInserted(lowerbound, 2);
            } else {
                addItem(lowerbound, new exerciseItem(category, exerciseShort, exerciseShort.getNote()));
                notifyItemInserted(lowerbound);
            }
            // The item is the highest item.
        } else if (itemToAdd.compareTo(upperItem) > 0) {
            if (!headers.contains(category)) {
                headers.add(category);
                addItem(upperbound + 1, new headerItem(category, category));
                addItem(upperbound + 2, new exerciseItem(category, exerciseShort, exerciseShort.getNote()));
                notifyItemRangeInserted(upperbound + 1, 2);
            } else {
                addItem(upperbound + 1, new exerciseItem(category, exerciseShort, exerciseShort.getNote()));
                notifyItemInserted(upperbound + 1);
            }
        } else { // The item is the middle.
            if (!headers.contains(category)) {
                headers.add(category);
                addItem(center, new headerItem(category, category));
                addItem(center + 1, new exerciseItem(category, exerciseShort, exerciseShort.getNote()));
                notifyItemRangeInserted(center, 2);
            } else {
                addItem(center, new exerciseItem(category, exerciseShort, exerciseShort.getNote()));
                notifyItemInserted(center);
            }
        }
    }

    private void onBindExerciseViewHolder(ExerciseListExerciseViewHolder holder, int position) {
        if (data.get(position) instanceof exerciseItem) {
            holder.getNameTextView().setText((data.get(position)).toString());

            // Create listener for the "more options" button. credit: Shaba Aafreen @https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item
            if (holder.getMoreOptionsView() != null) {
                holder.getMoreOptionsView().setOnClickListener(view -> {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(callback.getContext(), holder.getMoreOptionsView());
                    //inflating menu from xml resource
                    popup.inflate(R.menu.exercise_options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        int currentPosition = holder.getAdapterPosition();

                        switch (item.getItemId()) {
                            case R.id.menu_exercise_edit:
                                //handle menu1 click
                                callback.exerciseEdit(data.get(currentPosition).id());
                                return true;
                            case R.id.menu_exercise_perform:
                                //handle menu1 click
                                callback.exercisePerform((exerciseItem) data.get(currentPosition));//ex.getIdExercise(), ex.getName());
                                return true;
                            case R.id.menu_exercise_delete:
                                Long idExerciseToDelete = data.get(currentPosition).id();
                                ExerciseShort exerciseToDelete = getExerciseById(idExerciseToDelete); // [TODO] This is causing an error if we try to delete multiple exercises too many times.
                                deleteExercise(idExerciseToDelete);
                                callback.exerciseDelete(exerciseToDelete);
                                return true;
                            default:
                                return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                });
            }
        }
    }

    interface IViewHolder {
        void onDestroy();
    }

    private void onBindHeaderViewHolder(ExerciseListHeaderViewHolder holder, int position) {
        if (data.get(position) instanceof headerItem) {
            holder.getNameTextView().setText((data.get(position)).toString());
        }
    }

    private ExerciseShort getExerciseById(Long idExerciseToDelete) {
        for (int i = 0; i < originalData.size(); i++) {
            if (originalData.get(i).getIdExercise().equals(idExerciseToDelete))
                return (ExerciseShort) originalData.get(i);
        }

        throw new Resources.NotFoundException(String.valueOf(idExerciseToDelete) + " not found.");
    }

    private void deleteExercise(Long idExercise) {
        String category = "";
        int itemsInCategory = 0; // count of how many items are within a category. Does not include the header nor items to be removed.

        for (int i = data.size() - 1; i >= 0; i--) // We iterate bottom to top, so we can remove items within the loop.
        {
            IViewItem item = data.get(i);
            if (!item.category().equals(category)) // Reached a new category.
            {
                category = item.category();
                itemsInCategory = 0;
            }
            if (item instanceof exerciseItem && item.id().equals(idExercise)) {
                data.remove(i);
                notifyItemRemoved(i);
            } else {
                itemsInCategory += 1;
            }

            // If we reached the header and there are no child items being deleted (only the header is in the category), then we also need to remove the header.
            if (item instanceof headerItem && itemsInCategory == 1) {
                data.remove(i);
                headers.remove(category);
                notifyItemRemoved(i);
            }
        }
    }

    void filter(String query) {
        Log.d(TAG, "filter");
        List<IExerciseListable> filteredData = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            filteredData = originalData;
        } else {
            query = query.toLowerCase();

            for (IExerciseListable ex : originalData) {
                if (ex.getName().toLowerCase().contains(query)) {
                    filteredData.add(ex);
                }
            }
        }

        setData(filteredData); // [TODO] Re-initializing the whole data set will probably be slow. Better alternative is to remove exercises, and check if the category still has remaining exercises. If category does not have child exercises, then remove the header as well.
        //notifyDataSetChanged();
    }

    public interface IClickExercise {
        // When an exercise is clicked, send the clicked exercise.
        void exerciseEdit(Long idExercise);

        void exercisePerform(exerciseItem ex);//Long idExercise, String exerciseName);

        Context getContext();

        void exerciseDelete(ExerciseShort exerciseToDelete);
    }

    public abstract class IViewItem implements Comparable<IViewItem> {
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
            {
                int nameComparison = this.toString().compareToIgnoreCase(other.toString());
                if (nameComparison > 0) return 1;
                else if (nameComparison < 0) return -1;
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
        public String category() {
            return category;
        }

        @Override
        public Long id() {
            return Long.valueOf(itemType());
        }

        @Override
        int itemType() {
            return HEADER_TYPE;
        }
    }

    public class exerciseItem extends IViewItem {
        private String category;
        private String exerciseName;
        private Long idExercise;
        private String note;

        exerciseItem(String category, IExerciseListable exercise, String note) {
            this.idExercise = exercise.getIdExercise();
            this.exerciseName = exercise.getName();
            this.category = category;
            this.note = note;
        }

        // Values that do not require a category change was updated.
        void updateNoCategoryChanges(ExerciseUpdated ex) {
            exerciseName = ex.getName();
            note = ex.getNote();
        }

        @Override
        public String toString() {
            return exerciseName;
        }

        public String getNote() {
            return note;
        }

        @Override
        public String category() {
            return category;
        }

        @Override
        public Long id() {
            return idExercise;
        }

        @Override
        int itemType() {
            return EXERCISE_TYPE;
        }


    }
}
