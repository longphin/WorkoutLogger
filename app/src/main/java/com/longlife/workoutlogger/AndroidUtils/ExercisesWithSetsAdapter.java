package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets.RoutineCreateAddSetViewHolder;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateAdapter;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateViewHolder;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ExercisesWithSetsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_SET_TYPE = 3;
    private static final String TAG = RoutineCreateAdapter.class.getSimpleName();
    private static final int HEADER_TYPE = 1;
    private static final int SET_TYPE = 2;
    protected Context context;
    private List<RoutineExerciseHelper> exercisesToInclude = new ArrayList<>();

    // Other
    public ExercisesWithSetsAdapter(Context context) {
        this.context = context;
    }

    public static int getHeaderTypeEnum() {
        return HEADER_TYPE;
    }

    public void setExercisesToInclude(List<RoutineExerciseHelper> exercisesToInclude) {
        this.exercisesToInclude = exercisesToInclude;
    }

    public void setExercisesToInclude(ExerciseSessionWithSets exerciseWithSets) {
        exercisesToInclude.clear();
        exercisesToInclude.add(new RoutineExerciseHelper(exerciseWithSets));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case HEADER_TYPE:
                v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
                return new RoutineCreateViewHolder(v);
            case SET_TYPE:
                v = LayoutInflater.from(this.context).inflate(getSetLayout(), parent, false);
                return createSetViewHolder(v);
                /*
                v = LayoutInflater.from(this.context).inflate(getSetLayout(), parent, false);
                return new RoutineCreateSetViewHolder(v);
                */

            case ADD_SET_TYPE:
                v = LayoutInflater.from(this.context).inflate(R.layout.item_add_set, parent, false);
                return new RoutineCreateAddSetViewHolder(v);
            default:
                v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
                return new RoutineCreateViewHolder(v);
        }
    }

    public abstract int getSetLayout();

    public abstract RecyclerView.ViewHolder createSetViewHolder(View v);
    /*{
        return R.layout.item_routine_create_exercise_set;
    }
    */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        //ViewType viewType = viewTypes.get(position);

        if (holder instanceof RoutineCreateViewHolder) {
            bindHeaderViewHolder((RoutineCreateViewHolder) holder, position);
            return;
        }
        if (holder instanceof ExercisesWithSetsViewHolder) {
            bindSetViewHolder(holder, position);
            return;
        }
        if (holder instanceof RoutineCreateAddSetViewHolder) {
            bindAddSetViewHolder((RoutineCreateAddSetViewHolder) holder, position);
            return;
        }
    }

    @Override
    // [TODO] This currently iterates through all visible items and determines the type of the item at the end position. This is VERY inefficient. Make this use an array later.
    public int getItemViewType(int position) {
        int count = 0;

        for (RoutineExerciseHelper reh : exercisesToInclude) {
            if (count >= position)
                return HEADER_TYPE;

            if (reh.IsExpanded()) {
                count += reh.getSets().size();
                if (count >= position)
                    return SET_TYPE;

                count += 1;
                if (count >= position)
                    return ADD_SET_TYPE;
            }
            count += 1; // Iterate to next header.
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        int count = 0; // This is a count of the current item's position in the recyclerview.
        if (exercisesToInclude != null) {
            count += exercisesToInclude.size(); // Headers count.

            for (RoutineExerciseHelper reh : exercisesToInclude) {
                if (reh.IsExpanded()) {
                    count += reh.getSets().size(); // Sets count for each header.
                    count += 1; // Add 1 more for the "add set" button view.
                }
            }
        }
        return count;
    }

    public List<RoutineExerciseHelper> getRoutineExerciseHelpers() {
        return exercisesToInclude;
    }

    private void bindAddSetViewHolder(@NonNull RoutineCreateAddSetViewHolder holder, int position) {
        holder.getView().setOnClickListener(view ->
        {
            int pos = holder.getAdapterPosition();
            addSet(pos);
        });
    }

    private void addSet(int pos) {
        final int headerIndex = getHeaderIndex(pos);
        exercisesToInclude.get(headerIndex).getSets().add(new SessionExerciseSet());
        notifyItemInserted(pos);
    }

    private void bindHeaderViewHolder(@NonNull RoutineCreateViewHolder holder, int position) {
        final int headerIndex = getHeaderIndex(position);
        final RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);

        if (headerItem.IsExpanded()) {
            // [TODO] change arrow to point up
        } else {
            // change arrow to point down.
        }

        // Listener for when the "sets" box is clicked.
        holder.getNameTextView().setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            onHeaderClick(pos);
        });

        // Add on click listener to move a header up.
        if (headerIndex > 0) { // Only add the click listener when it is not the first item.

            holder.getUpButton().setOnClickListener(view -> {
                int pos = holder.getAdapterPosition();
                moveHeaderUp(getHeaderIndex(pos));
            });
        } else {
            // [TODO] if the item is the first item, hide the up arrow. holder.hideUpArrow()
        }

        if (headerIndex < exercisesToInclude.size() - 1) {

            holder.getDownButton().setOnClickListener(view -> {
                int pos = holder.getAdapterPosition();
                moveHeaderDown(getHeaderIndex(pos));
            });
        } else if (headerIndex == exercisesToInclude.size() - 1) {
            // [TODO] if item is the last item, hide the down arrow. holder.hideDownArrow()
        } else {
            throw new ArrayIndexOutOfBoundsException("Header index " + String.valueOf(headerIndex) + " not valid.");
        }

        final Exercise exercise = headerItem.getExercise();

        holder.setNameText(exercise.getName() + " (" + String.valueOf(exercise.getIdExercise()) + ")");
    }

    // When header is clicked, expand or collapse header.
    private void onHeaderClick(int headerPos) {
        final int headerIndex = getHeaderIndex(headerPos); // Get the index of the header clicked.
        RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
        final int childCount = headerItem.getSets().size();

        if (!headerItem.IsExpanded()) {
            // Is currently collapsed. Need to expand.
            headerItem.IsExpanded(true);
            notifyItemRangeInserted(headerPos + 1, childCount + 1); // childCount+1: The +1 is for the "add set" button view.
        } else {
            // Is currently expanded. Need to collapse.
            headerItem.IsExpanded(false);
            notifyItemRangeRemoved(headerPos + 1, childCount + 1); // childCount+1: The +1 is for the "add set" button view.
        }
    }

    // Move header up.
    private void moveHeaderUp(int headerIndex) {
        if (headerIndex == 0)
            return;

        moveHeader(headerIndex - 1, headerIndex);
    }

    // Move header down.
    private void moveHeaderDown(int headerIndex) {
        if (headerIndex >= exercisesToInclude.size() - 1)
            return;

        moveHeader(headerIndex, headerIndex + 1);
    }

    public abstract void bindSetViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);
    /*
    protected void bindSetViewHolder(@NonNull RoutineCreateSetViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        final SessionExerciseSet set = getSetAtPosition(pos);
        holder.getRestTextView().setText(context.getString(R.string.set_header, set.getRestMinutes(), set.getRestSeconds()));

        holder.getView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos));
        });
    }
    */

    protected SessionExerciseSet getSetAtPosition(int position) {
        int count = 0;

        for (int i = 0; i < exercisesToInclude.size(); i++) {//RoutineExerciseHelper reh : exercisesToInclude){
			/* // We don't care about the header.
			if(count >= position)
				return HEADER_TYPE;
			*/

            final RoutineExerciseHelper reh = exercisesToInclude.get(i);

            if (reh.IsExpanded()) {
                List<SessionExerciseSet> sets = reh.getSets();
                count += sets.size();
                if (count >= position) // This means the selected set is in this parent exercise, so find the exact set and return that id.
                {
                    count -= sets.size();

                    final int setIndex = position - count - 1;
                    return sets.get(setIndex);
                }

                count += 1; // Add 1 for the "Add set" view.
            }
            count += 1; // Iterate to next header.
        }

        return null;
    }

    // Helper for moving header up or down.
    // topHeaderIndex is the item higher in the recyclerview (lower adapter position),
    // bottomHeaderIndex is the item lower in the recyclerview (higher adapter position).
    private void moveHeader(int topHeaderIndex, int bottomHeaderIndex) {
        final int topHeaderPos = getHeaderPosition(topHeaderIndex);
        final int bottomHeaderPos = getHeaderPosition(bottomHeaderIndex);
        final int bottomChildren = exercisesToInclude.get(bottomHeaderIndex).getSets().size();
        Collections.swap(exercisesToInclude, topHeaderIndex, bottomHeaderIndex);

        final int rangeAffected = bottomHeaderPos - topHeaderPos
                + (exercisesToInclude.get(bottomHeaderIndex).IsExpanded() ? bottomChildren : 0) // If the bottom is expanded, we need to include the children as the bottom-most item affected.
                + 1; // Include the bottom-most "add set" button view.
        notifyItemRangeChanged(topHeaderPos, rangeAffected);
    }

    @Nullable
    protected RoutineExerciseSetPositions getIdSessionExerciseAtPosition(int position) {
        int count = 0;

        for (int i = 0; i < exercisesToInclude.size(); i++) {
            final RoutineExerciseHelper reh = exercisesToInclude.get(i);

            if (reh.IsExpanded()) {
                List<SessionExerciseSet> sets = reh.getSets();
                count += sets.size();
                if (count >= position) // This means the selected set is in this parent exercise, so find the exact set and return that id.
                {
                    count -= sets.size();

                    final int setIndex = position - count - 1;
                    final SessionExerciseSet set = sets.get(setIndex);
                    return new RoutineExerciseSetPositions(i, setIndex, set.getRestMinutes(), set.getRestSeconds(), reh.getExercise().getName(), set.getWeights(), set.getReps());
                }

                count += 1; // Add 1 for the "Add set" view.
            }
            count += 1; // Iterate to next header.
        }

        return null;
    }

    // Insert a list of exercises.
    public void addExercises(List<Exercise> ex) {
        final int currentSize = getItemCount(); // This is the recyclerview position that the items will be inserted after.

        for (Exercise e : ex) {
            exercisesToInclude.add(
                    new RoutineExerciseHelper(e,
                            //sets,
                            new ArrayList<>(Arrays.asList(new SessionExerciseSet())), // Each added exercise has 1 session exercise set by default.
                            true
                    )
            ); // Each added exercise will be expanded by default with 1 set.
        }
        notifyItemRangeInserted(currentSize, 3 * ex.size()); // 3*ex.size() because each exercise gets a header, a set, and a "add set" button.
    }

    // "Undo" the temporary delete of an exercise.
    public void restoreExercise(RoutineExerciseHelper ex, int headerIndex) {
        exercisesToInclude.add(headerIndex, ex);
        // Number of items restored.
        final int itemsRestored = ex.IsExpanded()
                ? ex.getSets().size() + 2 // +2 for the header and for the "add set" button view.
                : 1; // else, only 1 for the header
        notifyItemRangeInserted(getHeaderPosition(headerIndex), itemsRestored);
    }

    // Methods
    private int getHeaderPosition(int headerIndex) {
        if (headerIndex == 0)
            return 0; // If this is the first header item, then its position is 0.

        int count = 0;
        if (exercisesToInclude != null) {
            for (int i = 0; i < headerIndex; i++) {
                RoutineExerciseHelper headerItem = exercisesToInclude.get(i);
                if (headerItem.IsExpanded()) {

                    count += headerItem.getSets().size();

                    count += 1; // add 1 for "add" view
                }
                count += 1;
            }
        }
        return count;
    }

    // Attempts to swap two items in the recyclerview. If the items cannot be swapped.
    // i.e. cannot swap header with a set. If the items cannot be swapped, return false. Else return true.
    public boolean swap(int toPosition, int fromPosition) {
        // Check if the item being moved to is a sub item. If it is, then don't move the items.
        int newItemType = getItemViewType(toPosition);
        if (newItemType != HEADER_TYPE)
            return false;

        // Get the header index for both views.
        final int oldHeaderIndex = getHeaderIndex(fromPosition);
        final int newHeaderIndex = getHeaderIndex(toPosition);
        // If the headers are the same, then we do not need to move the items.
        if (oldHeaderIndex == newHeaderIndex)
            return false;

        // Swap the header positions.
        Collections.swap(exercisesToInclude, oldHeaderIndex, newHeaderIndex);

        return true;
    }

    // Get the header position given the recyclerview position.
    // Since some sub items take up adapter positions, this needs to iterate to see if headers are expanded.
    public int getHeaderIndex(int position) {
        int count = 0; // Keeps track of the iterator count for visible items.
        int headerCount = 0; // Keeps track of the header position.

        for (RoutineExerciseHelper reh : exercisesToInclude) {
            if (count >= position)
                return headerCount;

            if (reh.IsExpanded()) {

                count += reh.getSets().size() + 1; // The +1 is for the "add set" button view.
                if (count >= position)
                    return headerCount;
            }
            count += 1; // go to next header

            headerCount += 1;
        }

        throw new IndexOutOfBoundsException("Header not found for " + String.valueOf(position));
    }

    // Remove the item from the recyclerview at a position. It can be a header item or set item.
    public void removeItemAtPosition(int position) {
        int count = 0;
        for (RoutineExerciseHelper reh : exercisesToInclude) {
            // Check if the deleted item was a header item, so we need to remove the entire exercise.
            if (count == position) {
                removeExerciseAtPosition(position);
                return;
            }

            // Check if the deleted item may be a set item, which is only visible while expanded.
            if (reh.IsExpanded()) {
                for (int j = 0; j < reh.getSets().size(); j++) {
                    count += 1;
                    if (count == position) {
                        notifyItemRemoved(position);
                        reh.getSets().remove(j);
                        return;
                    }
                }
                count += 1; // Add 1 for the "add set" button view.
            }
            count += 1; // Iterate to next header.
        }
    }

    // Remove exercise given the recyclerview position.
    public void removeExerciseAtPosition(int position) {
        final int headerIndex = getHeaderIndex(position);
        final RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
        final int childSize = headerItem.getSets().size();

        if (headerItem.IsExpanded()) {
            notifyItemRangeRemoved(position, childSize + 1 + 1); // +1 for removing the header, +1 for removing the "add set" button view.
        } else {
            notifyItemRemoved(position); // Only the header needs to be removed.
        }
        // Remove the header item.
        exercisesToInclude.remove(headerIndex);
    }

    // Return the header item at the recyclerview position.
    public RoutineExerciseHelper getHeaderAtPosition(int position) {
        return exercisesToInclude.get(getHeaderIndex(position));
    }

    // An exercise was updated, so go through the exercises and update them.
    public void exerciseUpdated(Exercise exercise) {
        final Long idExercise = exercise.getIdExercise();

        for (int i = 0; i < exercisesToInclude.size(); i++) {
            if (exercisesToInclude.get(i).getExercise().getIdExercise().equals(idExercise)) {
                exercisesToInclude.get(i).setExercise(exercise);
                notifyItemChanged(getHeaderPosition(i));
            }
        }
    }

    public void setRestTimeForSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds) {
        // Check if the exercise to look up is within bounds.
        if (exerciseIndex < 0 || exerciseIndex > exercisesToInclude.size() - 1)
            return;
        List<SessionExerciseSet> exerciseSetsToAffect = exercisesToInclude.get(exerciseIndex).getSets();

        // Check if the setIndex is within bounds.
        if (exerciseSetIndex < 0 || exerciseSetIndex > exerciseSetsToAffect.size() - 1)
            return;
        exerciseSetsToAffect.get(exerciseSetIndex).setRest(restMinutes, restSeconds);

        notifySetChanged(exerciseIndex, exerciseSetIndex);
    }

    public void notifySetChanged(int exerciseIndex, int exerciseSetIndex) {
        // If the exercise is expanded, then update the timer being displayed.
        if (exercisesToInclude.get(exerciseIndex).IsExpanded()) {
            final int setPosition = getHeaderPosition(exerciseIndex) + exerciseSetIndex + 1;
            notifyItemChanged(setPosition);
        }
    }

    public void setWeightForSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds, @Nullable Double weight, @Nullable Integer reps) {
        // Check if the exercise to look up is within bounds.
        if (exerciseIndex < 0 || exerciseIndex > exercisesToInclude.size() - 1)
            return;
        List<SessionExerciseSet> exerciseSetsToAffect = exercisesToInclude.get(exerciseIndex).getSets();

        // Check if the setIndex is within bounds.
        if (exerciseSetIndex < 0 || exerciseSetIndex > exerciseSetsToAffect.size() - 1)
            return;
        SessionExerciseSet affectedSet = exerciseSetsToAffect.get(exerciseSetIndex);
        affectedSet.setRest(restMinutes, restSeconds);
        affectedSet.setWeights(weight);
        affectedSet.setReps(reps);

        notifySetChanged(exerciseIndex, exerciseSetIndex);
    }

    // Helper class specific for the set being edited. Contains the index of the position and the set position within that index.
    public class RoutineExerciseSetPositions {
        // Suppose we have exercises = List<Exercise>. Then for exercises.get(i), exerciseIndex = i.
        private int exerciseIndex;
        // Suppose we have exercises = List<Exercise>. Then for exercises.get(i).getSets().get(j), setIndexWithinExerciseIndex = j.
        private int setIndexWithinExerciseIndex;
        // Minutes of rest for the set.
        private int restMinutes;
        // Seconds of rest for the set.
        private int restSeconds;
        // Exercise name for this set.
        private String exerciseName;

        // Weights for the set.
        private Double weight;
        // Reps for the set.
        private Integer reps;

        public RoutineExerciseSetPositions(int exerciseIndex, int setIndexWithinExerciseIndex, int restMinutes, int restSeconds, String exerciseName, Double weight, Integer reps) {
            this(exerciseIndex, setIndexWithinExerciseIndex, restMinutes, restSeconds, exerciseName);

            this.weight = weight;
            this.reps = reps;
        }

        public RoutineExerciseSetPositions(int exerciseIndex, int setIndexWithinExerciseIndex, int restMinutes, int restSeconds, String exerciseName) {
            this.exerciseIndex = exerciseIndex;
            this.setIndexWithinExerciseIndex = setIndexWithinExerciseIndex;
            this.restMinutes = restMinutes;
            this.restSeconds = restSeconds;
            this.exerciseName = exerciseName;
        }

        public Double getWeight() {
            return weight;
        }

        public Integer getReps() {
            return reps;
        }

        public int getExerciseIndex() {
            return exerciseIndex;
        }

        public String getExerciseName() {
            return exerciseName;
        }

        public int getRestMinutes() {
            return restMinutes;
        }

        public int getRestSeconds() {
            return restSeconds;
        }

        public int getSetIndexWithinExerciseIndex() {
            return setIndexWithinExerciseIndex;
        }
    }
}
