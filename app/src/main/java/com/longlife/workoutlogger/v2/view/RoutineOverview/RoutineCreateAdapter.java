package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.SessionExerciseSet;
import com.longlife.workoutlogger.v2.utils.AdapterCallback;
import com.longlife.workoutlogger.v2.view.RoutineOverview.AddSets.RoutineCreateSetViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutineCreateAdapter
	extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
	// Static
	private static final String TAG = RoutineCreateAdapter.class.getSimpleName();
	private List<RoutineExerciseHelper> exercisesToInclude = new ArrayList<>();
	// OnClick callback to the parent fragment.
	private AdapterCallback callback;
	private static final int HEADER_TYPE = 1;
	private static final int SET_TYPE = 2;
	
	// Other
	//private List<ViewType> viewTypes = new ArrayList<>();
	
	private Context context;
	
	public RoutineCreateAdapter(Context context, AdapterCallback callback)
	{
		this.context = context;
		this.callback = callback;
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
			bindHeaderViewHolder(holder, position);
		}
		if(holder instanceof RoutineCreateSetViewHolder){
			bindSubViewHolder(holder, position);
		}
	}
	
	@Override
	public int getItemCount()
	{
		int count = 0; // This is a count of the current item's position in the recyclerview.
		if(exercisesToInclude != null){
			//viewTypes.clear();
			int collapsedCount = 0;
			for(int i = 0; i < exercisesToInclude.size(); i++){
				//viewTypes.add(count, new ViewType(i, HEADER_TYPE, i));//put(count, new ViewType(i, HEADER_TYPE));
				count += 1;
				
				RoutineExerciseHelper headerItem = exercisesToInclude.get(i);
				List<SessionExerciseSet> sets = headerItem.getSets();
				int childCount = sets.size();
				
				if(headerItem.IsExpanded()){
					// Expanded, count the children and add the children to viewTypes.
					for(int j = 0; j < childCount; j++){
						//viewTypes.add(count, new ViewType(count - (i + 1) + collapsedCount, SET_TYPE, i));
						count += 1;
					}
				}else{
					// Collapsed, keep track of children skipped.
					collapsedCount += childCount;
				}
			}
		}
		return count;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return getItemType(position);
		/*
		switch(viewTypes.get(position).getType()){
			case HEADER_TYPE:
				return HEADER_TYPE;
			case SET_TYPE:
				return SET_TYPE;
			default:
				return HEADER_TYPE;
		}
		*/
	}
	// Getters
	public static int getHeaderTypeEnum(){return HEADER_TYPE;}
	
	// Methods
	// [TODO] This currently iterates through all visible items and determines the type of the item at the end position. This is VERY inefficient. Make this use an array later.
	private int getHeaderIndex(int position)
	{
		int count = 0; // Keeps track of the iterator count for visible items.
		int headerCount = 0; // Keeps track of the header position.
		
		for(RoutineExerciseHelper reh : exercisesToInclude){
			if(count >= position)
				return headerCount;
			
			if(reh.IsExpanded()){
				
				count += reh.getSets().size();
				if(count >= position)
					return headerCount;
			}
			count += 1;
			
			headerCount += 1;
		}
		
		return -1; // Should be unreachable.
	}
	
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
				}
				count += 1;
			}
		}
		return count;
	}
	
	private void bindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
	{
		final int headerIndex = getHeaderIndex(position);
		final RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
		
		if(headerItem.IsExpanded()){
			// [TODO] change arrow to point up
		}else{
			// change arrow to point down.
		}
		
		// Listener for when the "sets" box is clicked.
		View.OnClickListener clickSet = new View.OnClickListener()
		{
			// Overrides
			@Override
			public void onClick(View view)
			{
				int pos = holder.getAdapterPosition();
				onHeaderClick(pos);
				Log.d(TAG, String.valueOf(pos) + " Clicked");
			}
		};
		((RoutineCreateViewHolder)holder).getDescripTextView().setOnClickListener(clickSet);
		
		((RoutineCreateViewHolder)holder).getUpButton().setOnClickListener(new View.OnClickListener()
		{
			// Overrides
			@Override
			public void onClick(View view)
			{
				int pos = holder.getAdapterPosition();
				moveHeaderUp(getHeaderIndex(pos));
			}
		});
		
		((RoutineCreateViewHolder)holder).getDownButton().setOnClickListener(new View.OnClickListener()
		{
			// Overrides
			@Override
			public void onClick(View view)
			{
				int pos = holder.getAdapterPosition();
				moveHeaderDown(getHeaderIndex(pos));
			}
		});
		
		final Exercise exercise = headerItem.getExercise();
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(exercise.getName())
			.append(" (")
			.append(exercise.getIdExercise())
			.append(")");
		
		((RoutineCreateViewHolder)holder).setNameText(sbName.toString());
	}
	
	private void moveHeaderUp(int headerIndex)
	{
		if(headerIndex == 0)
			return;
		Collections.swap(exercisesToInclude, headerIndex, headerIndex - 1);
		notifyItemMoved(headerIndex, headerIndex - 1);
	}
	
	private void moveHeaderDown(int headerIndex)
	{
		if(headerIndex >= exercisesToInclude.size() - 1)
			return;
		Collections.swap(exercisesToInclude, headerIndex, headerIndex + 1);
		notifyItemMoved(headerIndex, headerIndex + 1);
	}
	
	private void printElements()
	{
		int count = 0;
		for(int i = 0; i < exercisesToInclude.size(); i++){
			Log.d(TAG, String.valueOf(count) + ": " + exercisesToInclude.get(i).getExercise().getName() + ") " + (getItemType(count) == HEADER_TYPE ? "Head" : "   Set"));
			count += 1;
			if(exercisesToInclude.get(i).IsExpanded()){
				for(int j = 0; j < exercisesToInclude.get(i).getSets().size(); j++){
					Log.d(TAG, "-->" + String.valueOf(count) + ") " + (getItemType(count) == HEADER_TYPE ? "Head" : "   Set"));
					count += 1;
				}
			}
		}
	}
	
	private void bindSubViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
	{
	
	}
	
	public List<RoutineExerciseHelper> getRoutineExercises()
	{
		return exercisesToInclude;
	}
	
	private void onHeaderClick(int pos)
	{
		//ViewType viewType = viewTypes.get(pos);
		final int headerIndex = getHeaderIndex(pos); // Get the position of the header clicked.
		final int headerPosition = getHeaderPosition(headerIndex);
		RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
		List<SessionExerciseSet> sets = headerItem.getSets();
		final int childCount = sets.size();
		
		if(!headerItem.IsExpanded()){
			// Is currently collapsed. Need to expand.
			headerItem.IsExpanded(true);
			notifyItemRangeInserted(headerPosition + 1, childCount);
		}else{
			// Is currently expanded. Need to collapse.
			headerItem.IsExpanded(false);
			notifyItemRangeRemoved(headerPosition + 1, childCount);
		}
		
		printElements();
	}
	
	public void addExercise(@NonNull Exercise ex)
	{
		List<SessionExerciseSet> sets = new ArrayList<>();
		sets.add(new SessionExerciseSet());
		sets.add(new SessionExerciseSet());
		exercisesToInclude.add(new RoutineExerciseHelper(ex, sets, false));
		
		printElements();
	}
	
	public void addExercises(List<Exercise> ex)
	{
		int currentSize = exercisesToInclude.size();
		for(Exercise e : ex){
			List<SessionExerciseSet> sets = new ArrayList<>();
			sets.add(new SessionExerciseSet()); // [TODO] this is dummy data. Remove later.
			sets.add(new SessionExerciseSet());
			
			exercisesToInclude.add(new RoutineExerciseHelper(e, sets, false));
		}
		notifyItemRangeInserted(currentSize + 1, ex.size());
	}
	
	// [TODO] This currently iterates through all visible items and determines the type of the item at the end position. This is VERY inefficient. Make this use an array later.
	public int getItemType(int position)
	{
		int count = 0;
		
		for(RoutineExerciseHelper reh : exercisesToInclude){
			if(count >= position)
				return HEADER_TYPE;
			
			if(reh.IsExpanded()){
				
				
				count += reh.getSets().size();
				if(count >= position)
					return SET_TYPE;
			}
			count += 1;
			
		}
		
		return 0;
	}
	
	// "Undo" the temporary delete of an exercise.
	public void restoreExercise(RoutineExerciseHelper ex, int headerIndex)
	{
		ex.IsExpanded(false);
		exercisesToInclude.add(headerIndex, ex);
		notifyItemInserted(getHeaderPosition(headerIndex));
		
		printElements();
	}
	
	/*
	public void swap(int toPosition, int fromPosition)
	{
		ViewType newViewType = viewTypes.get(toPosition);
		if(newViewType.getType() == SET_TYPE)	return;
		ViewType oldViewType = viewTypes.get(fromPosition);
		
		// Get the header index for both views.
		final int oldHeaderIndex = oldViewType.getHeaderIndex();
		final int newHeaderIndex = newViewType.getHeaderIndex();
		// If the headers are the same, then we do not need to move the items.
		if(oldHeaderIndex == newHeaderIndex)
			return;
		
		Collections.swap(exercisesToInclude, newHeaderIndex, oldHeaderIndex);
		//notifyItemMoved(toPosition, fromPosition);
	}
	*/
	
	// [TODO] this is causing the drag move to stop sometimes?
	
	public boolean swap(int toPosition, int fromPosition)
	{
		// Check if the item being moved to is a sub item. If it is, then don't move the items.
		int newItemType = getItemType(toPosition);
		if(newItemType == SET_TYPE)
			return false;
		int oldItemType = getItemType(fromPosition);
		//ViewType oldViewType = viewTypes.get(fromPosition);
		
		// Get the header index for both views.
		final int oldHeaderIndex = getHeaderIndex(fromPosition);
		final int newHeaderIndex = getHeaderIndex(toPosition);
		// If the headers are the same, then we do not need to move the items.
		if(oldHeaderIndex == newHeaderIndex)
			return false;
		
		// Get the position of the headers for the recycler view.
		final int oldRecyclerPosition = getHeaderPosition(oldHeaderIndex); //[TODO] can probably just use fromPosition
		final int newRecyclerPosition = getHeaderPosition(newHeaderIndex); // [TODO] can probably just use toPosition
		// Swap the header positions.
		Log.d(TAG, String.valueOf(fromPosition) + " (" + String.valueOf(oldItemType == HEADER_TYPE ? "HEADER" : "SET") + ") to " + String.valueOf(toPosition) + " (" + String.valueOf(newItemType == HEADER_TYPE ? "HEADER" : "SET") + ")");
		
		Collections.swap(exercisesToInclude, oldHeaderIndex, newHeaderIndex);
		
		// If moving from top to down, then notify change from (old position, new position + new children)
		/*
		if(oldRecyclerPosition < newRecyclerPosition){
			notifyItemRangeChanged(oldRecyclerPosition, newRecyclerPosition + 1 - oldRecyclerPosition
				+ (exercisesToInclude.get(newHeaderIndex).IsExpanded() ? exercisesToInclude.get(newHeaderIndex).getSets().size() : 0));
		}else{ // else, notify from (new position, old position + old children)
			notifyItemRangeChanged(newRecyclerPosition, oldRecyclerPosition + 1 - newRecyclerPosition
				+ (exercisesToInclude.get(oldHeaderIndex).IsExpanded() ? exercisesToInclude.get(oldHeaderIndex).getSets().size() : 0));
		}
		*/
		return true;
	}
	
	public void removeExerciseAtPosition(int pos)
	{
		final int childSize = exercisesToInclude.get(pos).getSets().size();
		exercisesToInclude.remove(pos);
		//notifyItemRemoved(pos);
		notifyItemRangeRemoved(pos, childSize + 1);
		
		printElements();
	}
	
	public void removeItemAtPosition(int position)
	{
		int count = 0;
		
		for(RoutineExerciseHelper reh : exercisesToInclude){
			if(count == position){
				removeExerciseAtPosition(position);
				return;
			}
			
			if(reh.IsExpanded()){
				for(int j = 0; j < reh.getSets().size(); j++){
					count += 1;
					if(count == position){
						notifyItemRemoved(position);
						reh.getSets().remove(j);
						return;
					}
				}
			}
			count += 1;
		}
		
		printElements();
	}
	
	public RoutineExerciseHelper getHeaderAtPosition(int position)
	{
		return exercisesToInclude.get(getHeaderIndex(position));
	}
	
	
	/*
		@Override
	public void onViewMoved(int oldPosition, int newPosition)
	{
		// Check if the item being moved to is a sub item. If it is, then don't move the items.
		ViewType newViewType = viewTypes.get(newPosition);
		//if(newViewType.getType() == SET_TYPE)	return;
		ViewType oldViewType = viewTypes.get(oldPosition);
		
		// Get the header index for both views.
		final int oldHeaderIndex = oldViewType.getHeaderIndex();
		final int newHeaderIndex = newViewType.getHeaderIndex();
		// If the headers are the same, then we do not need to move the items.
		if(oldHeaderIndex == newHeaderIndex)
			return;
		
		// Get the position of the headers for the recycler view.
		final int oldRecyclerPosition = getHeaderPosition(oldHeaderIndex);
		final int newRecyclerPosition = getHeaderPosition(newHeaderIndex);
		// Swap the header positions.
		Log.d(TAG, String.valueOf(oldPosition) + " (" + String.valueOf(oldViewType.getType()) + ") to " + String.valueOf(newPosition) + " (" + String.valueOf(newViewType.getType()) + ")");
		
		Collections.swap(exercisesToInclude, oldHeaderIndex, newHeaderIndex);
		
		// If moving from top to down, then notify change from (old position, new position + new children)
		if(oldRecyclerPosition < newRecyclerPosition){
			notifyItemRangeChanged(oldRecyclerPosition, newRecyclerPosition + 1 - oldRecyclerPosition
				+ (exercisesToInclude.get(newHeaderIndex).IsExpanded() ? exercisesToInclude.get(newHeaderIndex).getSets().size() : 0));
		}else{ // else, notify from (new position, old position + old children)
			notifyItemRangeChanged(newRecyclerPosition, oldRecyclerPosition + 1 - newRecyclerPosition
				+ (exercisesToInclude.get(oldHeaderIndex).IsExpanded() ? exercisesToInclude.get(oldHeaderIndex).getSets().size() : 0));
		}
	}
	
	@Override
	public void onViewSwiped(int pos)
	{
		final int headerIndex = viewTypes.get(pos).getHeaderIndex();
		// Get the deleted group (header and sub items).
		final RoutineExerciseHelper item = exercisesToInclude.get(headerIndex);
		
		// Delete the item based on if it is a header item or a sub item.
		switch(viewTypes.get(pos).getType()){
			case HEADER_TYPE:
				final String name = item.getExercise().getName();
				
				// If the header is expanded, then remove the sub items as well.
				if(item.IsExpanded()){
					notifyItemRangeRemoved(pos, 1 + item.getSets().size());
				}else{ // else, only remove the header.
					notifyItemRemoved(pos);
				}
				exercisesToInclude.remove(headerIndex);
				
				// Show snack bar with Undo option.
				Snackbar snackbar = Snackbar
					.make(coordinatorLayout, name + " deleted.", Snackbar.LENGTH_LONG);
				snackbar.setAction("UNDO", view -> restoreExercise(item, headerIndex));
				snackbar.setActionTextColor(Color.YELLOW);
				snackbar.show();
				
				break;
			
			case SET_TYPE:
				// Get the sub item's header position within the recycler view.
				final int headerPos = getHeaderPosition(headerIndex);
				// Get the child's index within the header group.
				final int childIndex = pos - headerPos - 1;
				item.getSets().remove(childIndex);
				notifyItemRemoved(pos);
				break;
			
			default:
				break;
		}
	}
	 */
}
// Inner Classes
