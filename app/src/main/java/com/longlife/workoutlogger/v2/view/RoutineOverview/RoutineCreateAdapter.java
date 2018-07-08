package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.SessionExerciseSet;
import com.longlife.workoutlogger.v2.utils.SwipeAndDragHelper;
import com.longlife.workoutlogger.v2.utils.ViewType;
import com.longlife.workoutlogger.v2.view.RoutineOverview.AddSets.RoutineCreateSetViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutineCreateAdapter
	extends RecyclerView.Adapter<RecyclerView.ViewHolder>
	implements SwipeAndDragHelper.ActionCompletionContract
{
	// Static
	private static final String TAG = RoutineCreateAdapter.class.getSimpleName();
	private static final int HEADER_TYPE = 1;
	private static final int SUB_TYPE = 2;
	private List<RoutineExerciseHelper> exercisesToInclude = new ArrayList<>();
	private List<ViewType> viewTypes = new ArrayList<>();
	
	private Context context;
	private ItemTouchHelper touchHelper;
	private ConstraintLayout coordinatorLayout; // This is the layout used in the view. This is for the snackbar for Undo-ing deletes.
	
	public RoutineCreateAdapter(Context context)
	{
		this.context = context;
	}

	// Overrides
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos)
	{
		int position = holder.getAdapterPosition();
		ViewType viewType = viewTypes.get(position);
		
		if(holder instanceof RoutineCreateViewHolder){
			bindHeaderViewHolder(holder, position, viewType);
		}
		if(holder instanceof RoutineCreateSetViewHolder){
			bindSubViewHolder(holder, position, viewType);
		}
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v;
		
		switch(viewType){
			case HEADER_TYPE:
				v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
				return new RoutineCreateViewHolder(v);
			case SUB_TYPE:
				v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise_set, parent, false);
				return new RoutineCreateSetViewHolder(v);
			default:
				v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
				return new RoutineCreateViewHolder(v);
		}
	}

	@Override
	// Get a count of visible items, counting children if the header is expanded.
	public int getItemCount()
	{
		int count = 0; // This is a count of the current item's position in the recyclerview.
		if(exercisesToInclude != null){
			viewTypes.clear();
			int collapsedCount = 0;
			for(int i = 0; i < exercisesToInclude.size(); i++){
				viewTypes.add(count, new ViewType(i, HEADER_TYPE, i));//put(count, new ViewType(i, HEADER_TYPE));
				count += 1;
				
				RoutineExerciseHelper headerItem = exercisesToInclude.get(i);
				List<SessionExerciseSet> sets = headerItem.getSets();
				int childCount = sets.size();
				
				if(headerItem.IsExpanded()){
					// Expanded, count the children and add the children to viewTypes.
					for(int j = 0; j < childCount; j++){
						viewTypes.add(count, new ViewType(count - (i + 1) + collapsedCount, SUB_TYPE, i));
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
	public boolean isSwipeable(int position)
	{
		switch(viewTypes.get(position).getType()){
			case HEADER_TYPE:
				return true;
			case SUB_TYPE:
				return false;
			default:
				return false;
		}
	}
	
	@Override
	public int getItemViewType(int position)
	{
		switch(viewTypes.get(position).getType()){
			case HEADER_TYPE:
				return HEADER_TYPE;
			case SUB_TYPE:
				return SUB_TYPE;
			default:
				return HEADER_TYPE;
		}
	}
	
	@Override
	public boolean isDraggable(int position)
	{
		switch(viewTypes.get(position).getType()){
			case HEADER_TYPE:
				return true;
			case SUB_TYPE:
				return false;
			default:
				return false;
		}
	}
	
	@Override
	public void onViewMoved(int oldPosition, int newPosition)
	{
		// Check if the item being moved to is a sub item. If it is, then don't move the items.
		ViewType newViewType = viewTypes.get(newPosition);
		if(newViewType.getType() == SUB_TYPE)
			return;
		ViewType oldViewType = viewTypes.get(oldPosition);
		Log.d(TAG, String.valueOf(oldPosition) + " to " + String.valueOf(newPosition));
		
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
		Collections.swap(exercisesToInclude, oldHeaderIndex, newHeaderIndex);
		
		notifyItemRangeChanged(oldRecyclerPosition, newRecyclerPosition
			+ (exercisesToInclude.get(newHeaderIndex).IsExpanded() ? exercisesToInclude.get(newHeaderIndex).getSets().size() : 0));
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
			
			case SUB_TYPE:
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
	
	// Getters
	public List<RoutineExerciseHelper> getRoutineExercises()
	{
		return exercisesToInclude;
	}
	
	// Setters
	public void setCoordinatorLayout(ConstraintLayout coordinatorLayout)
	{
		this.coordinatorLayout = coordinatorLayout;
	}
	
	public void setTouchHelper(@NonNull ItemTouchHelper touchHelper)
	{
		this.touchHelper = touchHelper;
	}
	
	// Methods
	private int getHeaderPosition(int headerIndex)
	{
		if(headerIndex == 0)
			return 0; // If this is the first header item, then its position is 0.
		
		int count = 0;
		if(exercisesToInclude != null){
			for(int i = 0; i < headerIndex; i++){
				count += 1;
				RoutineExerciseHelper headerItem = exercisesToInclude.get(i);
				if(headerItem.IsExpanded()){
					count += headerItem.getSets().size();
				}
			}
		}
		return count;
	}
	
	private void bindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull ViewType viewType)
	{
		final int headerIndex = viewType.getHeaderIndex();
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
		
		// Listener for when to start dragging the item.
		View.OnTouchListener onTouchSetListener = new View.OnTouchListener()
		{
			// Overrides
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent)
			{
				if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
					touchHelper.startDrag(holder);
				}
				return false;
			}
		};
		((RoutineCreateViewHolder)holder).getDescripTextView().setOnTouchListener(onTouchSetListener); //[TODO] This warning is about making the touch event usable to visually-impaired users. Implement this later: https://stackoverflow.com/questions/47107105/android-button-has-setontouchlistener-called-on-it-but-does-not-override-perform
		
		final Exercise exercise = headerItem.getExercise();
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(exercise.getName())
			.append(" (")
			.append(exercise.getIdExercise())
			.append(")");
		
		((RoutineCreateViewHolder)holder).setNameText(sbName.toString());
	}
	
	private void bindSubViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull ViewType viewType)
	{
	
	}
	
	// "Undo" the temporary delete of an exercise.
	private void restoreExercise(@NonNull RoutineExerciseHelper ex, int headerIndex)
	{
		ex.IsExpanded(false);
		exercisesToInclude.add(headerIndex, ex);
		notifyItemInserted(getHeaderPosition(headerIndex));
	}
	
	private void onHeaderClick(int pos)
	{
		ViewType viewType = viewTypes.get(pos);
		int headerIndex = viewType.getHeaderIndex(); // Get the position of the header clicked.
		RoutineExerciseHelper headerItem = exercisesToInclude.get(headerIndex);
		List<SessionExerciseSet> sets = headerItem.getSets();
		int childCount = sets.size();
		
		if(!headerItem.IsExpanded()){
			// Is currently collapsed. Need to expand.
			headerItem.IsExpanded(true);
			notifyItemRangeInserted(pos + 1, childCount);
		}else{
			// Is currently expanded. Need to collapse.
			headerItem.IsExpanded(false);
			notifyItemRangeRemoved(pos + 1, childCount);
		}
	}
	
	private boolean isExpanded(int position)
	{
		return exercisesToInclude.get(position).IsExpanded();
	}
	
	public void addExercise(@NonNull Exercise ex)
	{
		List<SessionExerciseSet> sets = new ArrayList<>();
		sets.add(new SessionExerciseSet());
		sets.add(new SessionExerciseSet());
		exercisesToInclude.add(new RoutineExerciseHelper(ex, sets, false));
	}
	
	public void addExercises(@NonNull List<Exercise> ex)
	{
		int currentSize = exercisesToInclude.size();
		for(Exercise e : ex){
			List<SessionExerciseSet> sets = new ArrayList<>();
			sets.add(new SessionExerciseSet());
			sets.add(new SessionExerciseSet());
			
			exercisesToInclude.add(new RoutineExerciseHelper(e, sets, false));
		}
		notifyItemRangeInserted(currentSize + 1, ex.size());
	}
}
// Inner Classes
