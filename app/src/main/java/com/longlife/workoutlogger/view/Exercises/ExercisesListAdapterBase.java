/*
 * Created by Longphi Nguyen on 1/13/19 6:15 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/5/19 10:47 AM.
 */

package com.longlife.workoutlogger.view.Exercises;

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
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class ExercisesListAdapterBase extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int EXERCISE_TYPE = 1;
    protected List<IViewItem> data = new ArrayList<>();
    private Set<String> headers = new HashSet<>();
    private List<IExerciseListable> originalData;

    public ExercisesListAdapterBase(List<IExerciseListable> exercises, String query) {
        resetData(exercises, query);
    }

    void resetData(List<IExerciseListable> exercises, String query) {
        originalData = exercises;
        setData(exercises, query);
    }

    void filterData(String query) {
        setData(originalData, query);
    }

    void restoreExercise(ExerciseShort exerciseToRestore) {
        addExercise(exerciseToRestore);
    }

    // When inserting an exercise, split the data into two to see which chunk the new exercise should be inserted.
    void addExercise(ExerciseShort exerciseShort) {
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

    private void addItem(int pos, IViewItem item) {
        data.add(pos, item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EXERCISE_TYPE) return exerciseViewHolder(parent);
        return headerViewHolder(parent);
    }

    private RecyclerView.ViewHolder exerciseViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(exerciseItemLayout()
                , parent, false);
        return new ExerciseListExerciseViewHolder(v);
    }

    private RecyclerView.ViewHolder headerViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercises_header, parent, false);
        return new ExerciseListHeaderViewHolder(v);
    }

    protected abstract int exerciseItemLayout();

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

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder instanceof IViewHolder) {
            ((IViewHolder) holder).onDestroy();
        }
    }

    private void onBindHeaderViewHolder(ExerciseListHeaderViewHolder holder, int position) {
        if (data.get(position) instanceof headerItem) {
            holder.getNameTextView().setText((data.get(position)).toString());
        }
    }

    protected abstract void onBindExerciseViewHolder(ExerciseListExerciseViewHolder holder, int position);

    void exerciseUpdated(ExerciseUpdated updated, String query) {
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

        setData(originalData, query); // [TODO] Possible optimization: Because wasCategorization is not correct, logic to only update views when the category for an exercise changes.
        /*
        if(wasCategorizationChanged) // Need to reset the views because categorization was changed.
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

    protected ExerciseShort getExerciseById(Long idExerciseToDelete) {
        for (int i = 0; i < originalData.size(); i++) {
            if (originalData.get(i).getIdExercise().equals(idExerciseToDelete))
                return (ExerciseShort) originalData.get(i);
        }

        return null;
    }

    void deleteExercise(Long idExercise) {
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

    public void setData(List<IExerciseListable> exercises, String query) {
        data.clear();
        headers.clear();

        // 1. Iterate through the exercises and create a list item for the exercise.
        // 2. Then, create headers for the exercise categories also as a list item.
        Observable.fromIterable(exercises)
                .filter(ex -> query == null || query.isEmpty() || ex.getName().toLowerCase().contains(query))
                .map(ex ->
                {
                    String headerCategory = ex.getCategory();
                    headers.add(headerCategory); // Keep track of the exercise's category to create an item for the category later.
                    return new exerciseItem(headerCategory, ex, ex.getNote()); // The list item for the exercise.
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new SingleObserver<List<exerciseItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<exerciseItem> exerciseItems) {
                        // Add exercises to the list.
                        data.addAll(exerciseItems);

                        // Add headers to the list.
                        Observable.fromIterable(headers)
                                .map(headerName -> new headerItem(headerName, headerName))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .toList()
                                .subscribe(new SingleObserver<List<headerItem>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(List<headerItem> headerItems) {
                                        // Add list items for each category.
                                        data.addAll(headerItems);

                                        // Sort the exercise list items and the category list items.
                                        Collections.sort(data);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    interface IViewHolder {
        void onDestroy();
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

        public abstract Long id();
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

        public String getNote() {
            return note;
        }

        @Override
        public String toString() {
            return exerciseName;
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
