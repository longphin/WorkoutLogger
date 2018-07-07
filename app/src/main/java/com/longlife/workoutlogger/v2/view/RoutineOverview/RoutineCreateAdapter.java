package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.content.Context;
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
	private static final int HEADER_TYPE = 1;
	private static final int SUB_TYPE = 2;
	private List<RoutineExerciseHelper> exercisesToInclude = new ArrayList<>();
	// OnClick callback to the parent fragment.
	private AdapterCallback callback;
	
	//private SparseArray<ViewType> viewTypes;
	private List<ViewType> viewTypes = new ArrayList<>();
	//private SparseIntArray headerExpandTracker;
	private List<Boolean> headerExpandTracker = new ArrayList<>();
	
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
			case SUB_TYPE:
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
		int count = 0;
		if(exercisesToInclude != null){
			viewTypes.clear();
			int collapsedCount = 0;
			for(int i = 0; i < exercisesToInclude.size(); i++){
				viewTypes.add(count, new ViewType(i, HEADER_TYPE, i));//put(count, new ViewType(i, HEADER_TYPE));
				count += 1;
				List<SessionExerciseSet> sets = exercisesToInclude.get(i).getSets();
				int childCount = sets.size();
				if(headerExpandTracker.get(i)){
					// Expanded State
					for(int j = 0; j < childCount; j++){
						viewTypes.add(count, new ViewType(count - (i + 1) + collapsedCount, SUB_TYPE, i));
						count += 1;
					}
				}else{
					// Collapsed
					collapsedCount += childCount;
				}
			}
		}
		return count;
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
	
	// Getters
	public void bindHeaderViewHolder(RecyclerView.ViewHolder holder, int position, ViewType viewType)
	{
		int headerIndex = viewType.getHeaderIndex();
		Exercise exercise = exercisesToInclude.get(headerIndex).getExercise();
		
		if(isExpanded(headerIndex)){
			// change arrow to point up
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
				if(callback != null){
					int pos = holder.getAdapterPosition();
					//callback.onItemClicked(pos);
					callback.onHeaderClick(pos);
					Log.d(TAG, String.valueOf(pos) + " Clicked");
				}
			}
		};
		((RoutineCreateViewHolder)holder).getDescripTextView().setOnClickListener(clickSet); //[TODO] this click should start a fragment that starts a detail fragment.
		
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(exercise.getName())
			.append(" (")
			.append(exercise.getIdExercise())
			.append(")");
		
		((RoutineCreateViewHolder)holder).setNameText(sbName.toString());
		//holder.setDescripText(exercise.getDescription());
	}
	
	public void bindSubViewHolder(RecyclerView.ViewHolder holder, int position, ViewType viewType)
	{
	
	}
	public List<RoutineExerciseHelper> getRoutineExercises()
	{
		return exercisesToInclude;
	}
	
	public void addExercise(Exercise ex)
	{
		List<SessionExerciseSet> sets = new ArrayList<>();
		sets.add(new SessionExerciseSet());
		sets.add(new SessionExerciseSet());
		exercisesToInclude.add(new RoutineExerciseHelper(ex, sets));
		headerExpandTracker.add(false);
	}
	
	public void addExercises(List<Exercise> ex)
	{
		int currentSize = exercisesToInclude.size();
		//List<RoutineExerciseHelper> routineExercisesToAdd = new ArrayList<>();
		for(Exercise e : ex){
			//routineExercisesToAdd.add(new RoutineExerciseHelper(e));
			List<SessionExerciseSet> sets = new ArrayList<>();
			sets.add(new SessionExerciseSet());
			sets.add(new SessionExerciseSet());
			
			exercisesToInclude.add(new RoutineExerciseHelper(e, sets));
			headerExpandTracker.add(false);
		}
		//exercisesToInclude.addAll(routineExercisesToAdd);
		notifyItemRangeInserted(currentSize + 1, ex.size());
	}
	
	public void removeExerciseAtPosition(int pos)
	{
		exercisesToInclude.remove(pos);
		headerExpandTracker.remove(pos);
		notifyItemRemoved(pos);
	}
	
	// "Undo" the temporary delete of an exercise.
	public void restoreExercise(RoutineExerciseHelper ex, int position)
	{
		exercisesToInclude.add(position, ex);
		headerExpandTracker.add(position, false);
		notifyItemInserted(position);
	}
	
	public void swap(int toPosition, int fromPosition)
	{
		Collections.swap(exercisesToInclude, toPosition, fromPosition);
		Collections.swap(headerExpandTracker, toPosition, fromPosition);
		//notifyItemMoved(toPosition, fromPosition);
	}
	
	public void onHeaderClick(int pos)
	{
		ViewType viewType = viewTypes.get(pos);
		int headerIndex = viewType.getHeaderIndex();//getDataIndex(); // get the position of the header clicked.
		List<SessionExerciseSet> sets = exercisesToInclude.get(headerIndex).getSets();
		int childCount = sets.size();
		
		if(headerExpandTracker.get(headerIndex) == false){
			// Collapsed, to expand.
			headerExpandTracker.set(headerIndex, true);//add(position, true);
			notifyItemRangeInserted(headerIndex + 1, childCount);
		}else{
			// Expanded, to collapse.
			headerExpandTracker.set(headerIndex, false);
			notifyItemRangeRemoved(headerIndex + 1, childCount);
		}
	}
	
	public boolean isExpanded(int position)
	{
		return headerExpandTracker.get(position);
	}
	
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
}
// Inner Classes
