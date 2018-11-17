package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets.RoutineCreateAddSetViewHolder;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateAdapter;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateViewHolder;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public abstract class ExercisesWithSetsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = RoutineCreateAdapter.class.getSimpleName();

    public static final int HEADER_TYPE = 1;
    private static final int SET_TYPE = 2;
    private static final int ADD_SET_TYPE = 3;

    // Contains a list of exercises in this adapter.
    protected List<RoutineExerciseHelper> exercisesToInclude = new ArrayList<>();
    protected Context context;

    // Constructor.
    public ExercisesWithSetsAdapter(Context context) {
        this.context = context;
    }

    // Set the initial list of exercises.
    public void setExercisesToInclude(ExerciseSessionWithSets exerciseWithSets) {
        exercisesToInclude.clear();
        exercisesToInclude.add(new RoutineExerciseHelper(exerciseWithSets, 0));
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

            case ADD_SET_TYPE:
                v = LayoutInflater.from(this.context).inflate(R.layout.item_add_set, parent, false);
                return new RoutineCreateAddSetViewHolder(v);
            default:
                v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
                return new RoutineCreateViewHolder(v);
        }
    }

    // Classes that inherit this class need to specify what layout to use for sets.
    protected abstract int getSetLayout();

    // Classes that inherit this class need to specify the view holder for sets.
    protected abstract RecyclerView.ViewHolder createSetViewHolder(View v);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();

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

    // Get the list of exercises.
    public List<RoutineExerciseHelper> getRoutineExerciseHelpers() {
        return exercisesToInclude;
    }

    // Bind the "add set" view holder.
    private void bindAddSetViewHolder(@NonNull RoutineCreateAddSetViewHolder holder, int position) {
        holder.getView().setOnClickListener(view ->
        {
            int pos = holder.getAdapterPosition();
            addSet(pos);
        });
    }

    // Add a set at the adapter position. The header that this set is added to will be determined.
    private void addSet(int pos) {
        final int headerIndex = getHeaderIndex(pos);
        exercisesToInclude.get(headerIndex).getSets().add(new SessionExerciseSet());
        updateHeaderPositionsStartingAtIndex(headerIndex);
        notifyItemInserted(pos);

        printPositions();
    }

    // Get the header index given the adapter position.
    public int getHeaderIndex(int position) {
        for (int i = 0; i < exercisesToInclude.size(); i++) {
            if (isPositionInThisHeader(i, position)) {
                return i;
            }
        }

        throw new IndexOutOfBoundsException("Header not found for " + String.valueOf(position));
    }

    // Update header positions starting at a certain index. The starting index does not get its position updated, but is used to determine the subsequent header positions.
    // If the index < 0, then it will default to the first element.
    private void updateHeaderPositionsStartingAtIndex(int minIndex) {
        if (minIndex >= exercisesToInclude.size()) return;

        int visibleHeaderPosition = 0;
        if (minIndex < 0) { // Default to first element.
            minIndex = 0;
        } else {
            visibleHeaderPosition = exercisesToInclude.get(minIndex).getHeaderPosition();
        }

        for (int i = minIndex; i < exercisesToInclude.size(); i++) {
            RoutineExerciseHelper headerItem = exercisesToInclude.get(i);
            headerItem.setHeaderPosition(visibleHeaderPosition);
            if (headerItem.IsExpanded()) {
                visibleHeaderPosition += headerItem.getSets().size() + 2;
            } else {
                visibleHeaderPosition += 1;
            }
        }
    }

    // Check if the adapter position of within a header.
    private boolean isPositionInThisHeader(int headerIndex, int position) {
        return position >= exercisesToInclude.get(headerIndex).getHeaderPosition()
                && (
                (headerIndex + 1 < exercisesToInclude.size() // first, make sure that this current item is not the last one, then we can check if the set is within bounds.
                        && position < exercisesToInclude.get(headerIndex + 1).getHeaderPosition())
                        || headerIndex == exercisesToInclude.size() - 1
        );
    }

    // Get a set at the adapter position.
    protected SessionExerciseSet getSetAtPosition(int position) {
        int headerIndex = getHeaderIndex(position);
        int setIndex = getSetIndexWithinHeader(position);

        return exercisesToInclude.get(headerIndex).getSets().get(setIndex);
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

    // Bind header view holder.
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

        final ExerciseShort exercise = headerItem.getExercise();

        holder.setNameText(exercise.getName() + " (" + String.valueOf(exercise.getIdExercise()) + ")");
    }

    private void printPositions() {
        Log.d(TAG, "----------------");
        Log.d(TAG, Calendar.getInstance().getTime().toString());
        for (RoutineExerciseHelper reh : exercisesToInclude) {
            Log.d(TAG, String.valueOf(reh.getHeaderPosition()));
        }
    }

    // Bind set view holder.
    public abstract void bindSetViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

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
        updateHeaderPositionsStartingAtIndex(headerIndex);

        printPositions();
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

        // The header that is swapped to the top position will inherit the other's position. Headers after that will need their positions updated.
        final int minimumIndex = Math.min(topHeaderIndex, bottomHeaderIndex);
        final int maximumIndex = Math.max(topHeaderIndex, bottomHeaderIndex);
        exercisesToInclude.get(minimumIndex).setHeaderPosition(exercisesToInclude.get(maximumIndex).getHeaderPosition());
        updateHeaderPositionsStartingAtIndex(minimumIndex);//Math.min(topHeaderIndex, bottomHeaderIndex)-1);

        printPositions();
    }

    // Get the index of a set within a header.
    protected int getSetIndexWithinHeader(int position) {
        for (int i = 0; i < exercisesToInclude.size(); i++) {
            if (isSetPositionInThisHeader(i, position)) {
                return position - exercisesToInclude.get(i).getHeaderPosition() - 1;
            }
        }

        throw new IndexOutOfBoundsException("Set index not found for " + String.valueOf(position));
    }

    // Get the set for a given adapter position.
    @Nullable
    protected RoutineExerciseSetPositions getIdSessionExerciseAtPosition(int position) {
        int headerIndex = getHeaderIndex(position);
        int setIndex = getSetIndexWithinHeader(position);

        SessionExerciseSet set = exercisesToInclude.get(headerIndex).getSets().get(setIndex);
        return new RoutineExerciseSetPositions(headerIndex, setIndex, set, exercisesToInclude.get(headerIndex).getExercise().getName());
    }

    // Attempts to swap two items in the recyclerview. Returns whether the items can be swapped or not.
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

        // Update visible positions after the swap. The top-most item will inherit the other's header position. The rest of the headers will need their positions updated.
        final int minimumIndex = Math.min(oldHeaderIndex, newHeaderIndex);
        final int maximumIndex = Math.max(oldHeaderIndex, newHeaderIndex);
        exercisesToInclude.get(minimumIndex).setHeaderPosition(exercisesToInclude.get(maximumIndex).getHeaderPosition());
        updateHeaderPositionsStartingAtIndex(minimumIndex);

        return true;
    }

    // Get the item type at a given adapter position.
    @Override
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

    // The item count is the number of visible items.
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

    // Insert a list of exercises.
    public void addExercises(List<ExerciseShort> ex) {
        if (exercisesToInclude == null)
            exercisesToInclude = new ArrayList<>();

        final int currentSize = getItemCount(); // This is the recyclerview position that the items will be inserted after.
        int visibleHeaderPosition = currentSize;

        for (ExerciseShort e : ex) {
            exercisesToInclude.add(
                    new RoutineExerciseHelper(e,
                            //sets,
                            new ArrayList<>(Arrays.asList(new SessionExerciseSet())), // Each added exercise has 1 session exercise set by default.
                            true,
                            visibleHeaderPosition
                    )
            ); // Each added exercise will be expanded by default with 1 set.

            visibleHeaderPosition += 3; // header + 1 default set + add set button
        }
        notifyItemRangeInserted(currentSize, 3 * ex.size()); // 3*ex.size() because each exercise gets a header, a set, and a "add set" button.
        //updateHeaderPositionsStartingAtIndex(currentLastIndex); // No need to update, because the positions are given when inserted already.
        printPositions();
    }

    // "Undo" the temporary delete of an exercise.
    public void restoreExercise(RoutineExerciseHelper ex, int headerIndex) {
        exercisesToInclude.add(headerIndex, ex);
        // Number of items restored.
        final int itemsRestored = ex.IsExpanded()
                ? ex.getSets().size() + 2 // +2 for the header and for the "add set" button view.
                : 1; // else, only 1 for the header
        notifyItemRangeInserted(getHeaderPosition(headerIndex), itemsRestored);
        updateHeaderPositionsStartingAtIndex(headerIndex - 1);
        printPositions();
    }

    // Remove the item from the recyclerview at an adapter position. It can be a header item or set item.
    public void removeItemAtPosition(int position) {
        for (int i = 0; i < exercisesToInclude.size(); i++) {
            RoutineExerciseHelper currentHeader = exercisesToInclude.get(i);
            if (position == currentHeader.getHeaderPosition()) // The item being removed is a header item.
            {
                removeExerciseAtIndex(i);
                return;
            }

            if (isSetPositionInThisHeader(i, position)) {
                int childPosition = position - currentHeader.getHeaderPosition() - 1;
                removeSetFromExercise(i, childPosition);
                return;
            }
        }
    }

    // Remove a header item.
    private void removeExerciseAtIndex(int headerIndex) {
        if (headerIndex < 0 || headerIndex >= exercisesToInclude.size()) {
            return;
        }

        final RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
        final int childSize = headerItem.getSets().size();

        if (headerItem.IsExpanded()) {
            notifyItemRangeRemoved(headerItem.getHeaderPosition(), childSize + 1 + 1); // +1 for removing the header, +1 for removing the "add set" button view.
        } else {
            notifyItemRemoved(headerItem.getHeaderPosition()); // Only the header needs to be removed.
        }

        exercisesToInclude.remove(headerIndex);
        updateHeaderPositionsStartingAtIndex(headerIndex - 1);

        printPositions();
    }

    // Check if a non-header position is in the given header.
    private boolean isSetPositionInThisHeader(int headerIndex, int position) {
        return position > exercisesToInclude.get(headerIndex).getHeaderPosition()
                && (
                (headerIndex + 1 < exercisesToInclude.size() // first, make sure that this current item is not the last one, then we can check if the set is within bounds.
                        && position < exercisesToInclude.get(headerIndex + 1).getHeaderPosition())
                        || headerIndex == exercisesToInclude.size() - 1
        );
    }

    /**
     * Remove a set from a header.
     *
     * @param headerIndex The header's index.
     * @param setIndex    The set's index within the header index.
     */
    private void removeSetFromExercise(int headerIndex, int setIndex) {
        RoutineExerciseHelper currentHeader = exercisesToInclude.get(headerIndex);
        currentHeader.getSets().remove(setIndex);
        if (currentHeader.IsExpanded()) {
            notifyItemRemoved(currentHeader.getHeaderPosition() + setIndex + 1);
            updateHeaderPositionsStartingAtIndex(headerIndex);
        }
        printPositions();
    }

    // Get the header at the adapter position.
    public RoutineExerciseHelper getHeaderAtPosition(int position) {
        return exercisesToInclude.get(getHeaderIndex(position));
    }

    // Remove exercise given the adapter position.
    public void removeExerciseAtPosition(int position) {
        final int headerIndex = getHeaderIndex(position);
        removeExerciseAtIndex(headerIndex);
    }

    /**
     * Change the rest time for a set.
     *
     * @param exerciseIndex    The header index.
     * @param exerciseSetIndex The set index within the header index.
     * @param restMinutes      The minutes to rest.
     * @param restSeconds      The seconds to rest.
     */
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

        printPositions();
    }

    // An exercise was updated, so go through the exercises and update them.
    public void exerciseUpdated(ExerciseUpdated updatedExercise) {
        final Long idExercise = updatedExercise.getIdExercise();

        for (int i = 0; i < exercisesToInclude.size(); i++) {
            ExerciseShort exerciseToMaybeUpdate = exercisesToInclude.get(i).getExercise();
            if (exerciseToMaybeUpdate.getIdExercise().equals(idExercise)) {
                exerciseToMaybeUpdate.update(updatedExercise);
                notifyItemChanged(getHeaderPosition(i));
            }
        }

        printPositions();
    }

    /**
     * A set was changed, so trigger some changes.
     * @param exerciseIndex The header index.
     * @param exerciseSetIndex The set index within the header index.
     */
    private void notifySetChanged(int exerciseIndex, int exerciseSetIndex) {
        // If the exercise is expanded, then update the set.
        if (exercisesToInclude.get(exerciseIndex).IsExpanded()) {
            final int setPosition = getHeaderPosition(exerciseIndex) + exerciseSetIndex + 1;
            notifyItemChanged(setPosition);
        }
    }

    // Get the visible adapter position for a header.
    private int getHeaderPosition(int headerIndex) {
        return exercisesToInclude.get(headerIndex).getHeaderPosition();
    }

    /**
     * Change the weights for a set.
     * @param exerciseIndex The header index.
     * @param exerciseSetIndex The set index within the header index.
     * @param restMinutes The minutes to rest.
     * @param restSeconds The seconds to rest.
     * @param weight The weight for the set.
     * @param reps The number of reps for the set.
     * @param weightUnit The units that the weights are in, such as lbs, kgs.
     */
    public void setWeightForSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds, @Nullable Double weight, @Nullable Integer reps, int weightUnit) {
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
        affectedSet.setWeightUnit(weightUnit);

        notifySetChanged(exerciseIndex, exerciseSetIndex);

        printPositions();
    }

    /**
     * Helper class specific for the set being edited. Contains the index of the position and the set position within that index.
     */
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
        // Weight Unit type.
        private int weightUnit;
        // Reps for the set.
        private Integer reps;

        private RoutineExerciseSetPositions(int exerciseIndex, int setIndexWithinExerciseIndex, SessionExerciseSet ses, String exerciseName) {
            this(exerciseIndex, setIndexWithinExerciseIndex, ses.getRestMinutes(), ses.getRestSeconds(), exerciseName);

            this.weight = ses.getWeights();
            this.reps = ses.getReps();
            this.weightUnit = ses.getWeightUnit();
        }

        private RoutineExerciseSetPositions(int exerciseIndex, int setIndexWithinExerciseIndex, int restMinutes, int restSeconds, String exerciseName) {
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

        public int getWeightUnit() {
            return weightUnit;
        }
    }
}
