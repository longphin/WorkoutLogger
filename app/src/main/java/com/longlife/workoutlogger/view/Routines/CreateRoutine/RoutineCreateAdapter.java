package com.longlife.workoutlogger.view.Routines.CreateRoutine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets.RoutineCreateAddSetViewHolder;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets.RoutineCreateSetViewHolder;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoutineCreateAdapter
	extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
	// Static
	private static final int ADD_SET_TYPE = 3;
	private static final String TAG = RoutineCreateAdapter.class.getSimpleName();
	private static final int HEADER_TYPE = 1;
	private static final int SET_TYPE = 2;
	private List<RoutineExerciseHelper> exercisesToInclude = new ArrayList<>();
	private Context context;
	
	// Other
	public RoutineCreateAdapter(Context context)
	{
		this.context = context;
	}
	
	// Overrides
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v;
		
		switch(viewType){
			case HEADER_TYPE:
				v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
				return new RoutineCreateViewHolder(v);
			case SET_TYPE:
				v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise_set, parent, false);
				return new RoutineCreateSetViewHolder(v);
			case ADD_SET_TYPE:
				v = LayoutInflater.from(this.context).inflate(R.layout.item_add_set, parent, false);
				return new RoutineCreateAddSetViewHolder(v);
			default:
				v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
				return new RoutineCreateViewHolder(v);
		}
	}
	
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos)
	{
		int position = holder.getAdapterPosition();
		//ViewType viewType = viewTypes.get(position);
		
		if(holder instanceof RoutineCreateViewHolder){
			bindHeaderViewHolder((RoutineCreateViewHolder)holder, position);
			return;
		}
		if(holder instanceof RoutineCreateSetViewHolder){
			bindSubViewHolder((RoutineCreateSetViewHolder)holder, position);
			return;
		}
		if(holder instanceof RoutineCreateAddSetViewHolder){
			bindAddSetViewHolder((RoutineCreateAddSetViewHolder)holder, position);
			return;
		}
	}
	
	@Override
	public int getItemCount()
	{
		int count = 0; // This is a count of the current item's position in the recyclerview.
		if(exercisesToInclude != null){
			count += exercisesToInclude.size(); // Headers count.
			
			for(RoutineExerciseHelper reh : exercisesToInclude){
				if(reh.IsExpanded()){
					count += reh.getSets().size(); // Sets count for each header.
					count += 1; // Add 1 more for the "add set" button view.
				}
			}
		}
		return count;
	}
	
	@Override
	// [TODO] This currently iterates through all visible items and determines the type of the item at the end position. This is VERY inefficient. Make this use an array later.
	public int getItemViewType(int position)
	{
		int count = 0;
		
		for(RoutineExerciseHelper reh : exercisesToInclude){
			if(count >= position)
				return HEADER_TYPE;
			
			if(reh.IsExpanded()){
				count += reh.getSets().size();
				if(count >= position)
					return SET_TYPE;
				
				count += 1;
				if(count >= position)
					return ADD_SET_TYPE;
			}
			count += 1; // Iterate to next header.
		}
		
		return 0;
	}
	
	// Getters
	public static int getHeaderTypeEnum(){return HEADER_TYPE;}
	
	public List<RoutineExerciseHelper> getRoutineExerciseHelpers()
	{
		return exercisesToInclude;
	}
	
	// Methods
	// [TODO] This currently iterates through all visible items and determines the type of the item at the end position. This is VERY inefficient. Make this use an array later.
	private int getHeaderPosition(int headerIndex)
	{
		if(headerIndex == 0)
			return 0; // If this is the first header item, then its position is 0.
		
		int count = 0;
		if(exercisesToInclude != null){
			for(int i = 0; i < headerIndex; i++){
				RoutineExerciseHelper headerItem = exercisesToInclude.get(i);
				if(headerItem.IsExpanded()){
					
					count += headerItem.getSets().size();
					
					count += 1; // add 1 for "add" view
				}
				count += 1;
			}
		}
		return count;
	}
	
	private void bindAddSetViewHolder(@NonNull RoutineCreateAddSetViewHolder holder, int position)
	{
		holder.getView().setOnClickListener(view ->
		{
			int pos = holder.getAdapterPosition();
			addSet(pos);
		});
	}
	
	private void addSet(int pos)
	{
		final int headerIndex = getHeaderIndex(pos);
		final int childrenCount = exercisesToInclude.get(headerIndex).getSets().size();
		exercisesToInclude.get(headerIndex).getSets().add(new SessionExerciseSet());
		notifyItemInserted(pos + childrenCount + 1);
	}
	
	private void bindHeaderViewHolder(@NonNull RoutineCreateViewHolder holder, int position)
	{
		final int headerIndex = getHeaderIndex(position);
		final RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
		
		if(headerItem.IsExpanded()){
			// [TODO] change arrow to point up
		}else{
			// change arrow to point down.
		}
		
		// Listener for when the "sets" box is clicked.
		holder.getNameTextView().setOnClickListener(view -> {
			int pos = holder.getAdapterPosition();
			onHeaderClick(pos);
		});
		
		// Add on click listener to move a header up.
		if(headerIndex > 0){ // Only add the click listener when it is not the first item.
			// Overrides
			holder.getUpButton().setOnClickListener(view -> {
				int pos = holder.getAdapterPosition();
				moveHeaderUp(getHeaderIndex(pos));
			});
		}else{
			// [TODO] if the item is the first item, hide the up arrow. holder.hideUpArrow()
		}
		
		if(headerIndex < exercisesToInclude.size() - 1){
			// Overrides
			holder.getDownButton().setOnClickListener(view -> {
				int pos = holder.getAdapterPosition();
				moveHeaderDown(getHeaderIndex(pos));
			});
		}else if(headerIndex == exercisesToInclude.size() - 1){
			// [TODO] if item is the last item, hide the down arrow. holder.hideDownArrow()
		}else{
			throw new ArrayIndexOutOfBoundsException("Header index " + String.valueOf(headerIndex) + " not valid.");
		}
		
		final Exercise exercise = headerItem.getExercise();
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(exercise.getName())
			.append(" (")
			.append(exercise.getIdExercise())
			.append(")");
		
		holder.setNameText(sbName.toString());
	}
	
	// When header is clicked, expand or collapse header.
	private void onHeaderClick(int headerPos)
	{
		final int headerIndex = getHeaderIndex(headerPos); // Get the index of the header clicked.
		RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
		final int childCount = headerItem.getSets().size();
		
		if(!headerItem.IsExpanded()){
			// Is currently collapsed. Need to expand.
			headerItem.IsExpanded(true);
			notifyItemRangeInserted(headerPos + 1, childCount + 1); // childCount+1: The +1 is for the "add set" button view.
		}else{
			// Is currently expanded. Need to collapse.
			headerItem.IsExpanded(false);
			notifyItemRangeRemoved(headerPos + 1, childCount + 1); // childCount+1: The +1 is for the "add set" button view.
		}
	}
	
	// Move header up.
	private void moveHeaderUp(int headerIndex)
	{
		if(headerIndex == 0)
			return;
		
		moveHeader(headerIndex - 1, headerIndex);
	}
	
	// Move header down.
	private void moveHeaderDown(int headerIndex)
	{
		if(headerIndex >= exercisesToInclude.size() - 1)
			return;
		
		moveHeader(headerIndex, headerIndex + 1);
	}
	
	private void bindSubViewHolder(@NonNull RoutineCreateSetViewHolder holder, int position)
	{
	
	}
	
	// Helper for moving header up or down.
	// topHeaderIndex is the item higher in the recyclerview (lower adapter position),
	// bottomHeaderIndex is the item lower in the recyclerview (higher adapter position).
	private void moveHeader(int topHeaderIndex, int bottomHeaderIndex)
	{
		final int topHeaderPos = getHeaderPosition(topHeaderIndex);
		final int bottomHeaderPos = getHeaderPosition(bottomHeaderIndex);
		final int bottomChildren = exercisesToInclude.get(bottomHeaderIndex).getSets().size();
		Collections.swap(exercisesToInclude, topHeaderIndex, bottomHeaderIndex);
		
		final int rangeAffected = bottomHeaderPos - topHeaderPos
			+ (exercisesToInclude.get(bottomHeaderIndex).IsExpanded() ? bottomChildren : 0) // If the bottom is expanded, we need to include the children as the bottom-most item affected.
			+ 1; // Include the bottom-most "add set" button view.
		notifyItemRangeChanged(topHeaderPos, rangeAffected);
	}
	
	// Get the header position given the recyclerview position.
	// Since some sub items take up adapter positions, this needs to iterate to see if headers are expanded.
	public int getHeaderIndex(int position)
	{
		int count = 0; // Keeps track of the iterator count for visible items.
		int headerCount = 0; // Keeps track of the header position.
		
		for(RoutineExerciseHelper reh : exercisesToInclude){
			if(count >= position)
				return headerCount;
			
			if(reh.IsExpanded()){
				
				count += reh.getSets().size() + 1; // The +1 is for the "add set" button view.
				if(count >= position)
					return headerCount;
			}
			count += 1; // go to next header
			
			headerCount += 1;
		}
		
		throw new IndexOutOfBoundsException("Header not found for " + String.valueOf(position));
	}
	
	// Insert a list of exercises.
	public void addExercises(List<Exercise> ex)
	{
		final int currentSize = getItemCount(); // This is the recyclerview position that the items will be inserted after.
		
		for(Exercise e : ex){
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
	public void restoreExercise(RoutineExerciseHelper ex, int headerIndex)
	{
		exercisesToInclude.add(headerIndex, ex);
		// Number of items restored.
		final int itemsRestored = ex.IsExpanded()
			? ex.getSets().size() + 2 // +2 for the header and for the "add set" button view.
			: 1; // else, only 1 for the header
		notifyItemRangeInserted(getHeaderPosition(headerIndex), itemsRestored);
	}
	
	// Attempts to swap two items in the recyclerview. If the items cannot be swapped.
	// i.e. cannot swap header with a set. If the items cannot be swapped, return false. Else return true.
	public boolean swap(int toPosition, int fromPosition)
	{
		// Check if the item being moved to is a sub item. If it is, then don't move the items.
		int newItemType = getItemViewType(toPosition);
		if(newItemType != HEADER_TYPE)
			return false;
		
		// Get the header index for both views.
		final int oldHeaderIndex = getHeaderIndex(fromPosition);
		final int newHeaderIndex = getHeaderIndex(toPosition);
		// If the headers are the same, then we do not need to move the items.
		if(oldHeaderIndex == newHeaderIndex)
			return false;
		
		// Swap the header positions.
		Collections.swap(exercisesToInclude, oldHeaderIndex, newHeaderIndex);
		
		return true;
	}
	
	// Remove exercise given the recyclerview position.
	public void removeExerciseAtPosition(int position)
	{
		final int headerIndex = getHeaderIndex(position);
		final RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
		final int childSize = headerItem.getSets().size();
		
		if(headerItem.IsExpanded()){
			notifyItemRangeRemoved(position, childSize + 1 + 1); // +1 for removing the header, +1 for removing the "add set" button view.
		}else{
			notifyItemRemoved(position); // Only the header needs to be removed.
		}
		// Remove the header item.
		exercisesToInclude.remove(headerIndex);
	}
	
	// Remove the item from the recyclerview at a position. It can be a header item or set item.
	public void removeItemAtPosition(int position)
	{
		int count = 0;
		for(RoutineExerciseHelper reh : exercisesToInclude){
			// Check if the deleted item was a header item, so we need to remove the entire exercise.
			if(count == position){
				removeExerciseAtPosition(position);
				return;
			}
			
			// Check if the deleted item may be a set item, which is only visible while expanded.
			if(reh.IsExpanded()){
				for(int j = 0; j < reh.getSets().size(); j++){
					count += 1;
					if(count == position){
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
	
	// Return the header item at the recyclerview position.
	public RoutineExerciseHelper getHeaderAtPosition(int position)
	{
		return exercisesToInclude.get(getHeaderIndex(position));
	}
}
// Inner Classes
