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
import com.longlife.workoutlogger.v2.utils.ViewType;
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
	List<ViewType> viewTypes = new ArrayList<>();
	
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
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos)
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
						viewTypes.add(count, new ViewType(count - (i + 1) + collapsedCount, SET_TYPE, i));
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
	
	// Getters
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
	
	public List<RoutineExerciseHelper> getRoutineExercises()
	{
		return exercisesToInclude;
	}
	
	private void onHeaderClick(int pos)
	{
		ViewType viewType = viewTypes.get(pos);
		final int headerIndex = viewType.getHeaderIndex(); // Get the position of the header clicked.
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
	}
	
	public void addExercise(@NonNull Exercise ex)
	{
		List<SessionExerciseSet> sets = new ArrayList<>();
		sets.add(new SessionExerciseSet());
		sets.add(new SessionExerciseSet());
		exercisesToInclude.add(new RoutineExerciseHelper(ex, sets, false));
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
	
	public void removeExerciseAtPosition(int pos)
	{
		exercisesToInclude.remove(pos);
		notifyItemRemoved(pos);
	}
	
	// "Undo" the temporary delete of an exercise.
	public void restoreExercise(RoutineExerciseHelper ex, int headerIndex)
	{
		ex.IsExpanded(false);
		exercisesToInclude.add(headerIndex, ex);
		notifyItemInserted(getHeaderPosition(headerIndex));
	}
	
	public void swap(int toPosition, int fromPosition)
	{
		Collections.swap(exercisesToInclude, toPosition, fromPosition);
		//notifyItemMoved(toPosition, fromPosition);
	}
}
// Inner Classes
