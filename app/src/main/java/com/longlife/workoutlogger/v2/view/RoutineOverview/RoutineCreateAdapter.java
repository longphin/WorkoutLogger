package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.AdapterCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutineCreateAdapter
	extends RecyclerView.Adapter<RoutineCreateViewHolder>
{
	// Static
	private static final String TAG = RoutineCreateAdapter.class.getSimpleName();
	private List<RoutineExerciseHelper> exercisesToInclude = new ArrayList<>();
	// OnClick callback to the parent fragment.
	private AdapterCallback callback;
	
	private Context context;
	
	public RoutineCreateAdapter(Context context, AdapterCallback callback)
	{
		this.context = context;
		this.callback = callback;
	}
	
	// Overrides
	@Override
	public RoutineCreateViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
		
		return new RoutineCreateViewHolder(v);
	}

	@Override
	public void onBindViewHolder(RoutineCreateViewHolder holder, int pos)
	{
		int position = holder.getAdapterPosition();
		Exercise exercise = exercisesToInclude.get(position).getExercise();
		
		// Listener for when the "sets" box is clicked.
		View.OnClickListener clickSet = new View.OnClickListener()
		{
			// Overrides
			@Override
			public void onClick(View view)
			{
				if(callback != null){
					int pos = holder.getAdapterPosition();
					callback.onItemClicked(pos);
					Log.d(TAG, String.valueOf(pos) + " Clicked");
				}
			}
		};
		holder.getDescripTextView().setOnClickListener(clickSet); //[TODO] this click should start a fragment that starts a detail fragment.
		
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(exercise.getName())
			.append(" (")
			.append(exercise.getIdExercise())
			.append(")");
		
		holder.setNameText(sbName.toString());
		//holder.setDescripText(exercise.getDescription());
	}
	
	@Override
	public int getItemCount()
	{
		return exercisesToInclude.size();
	}
	
	// Getters
	public List<RoutineExerciseHelper> getRoutineExercises()
	{
		return exercisesToInclude;
	}
	
	public void addExercise(Exercise ex)
	{
		exercisesToInclude.add(new RoutineExerciseHelper(ex));
	}
	
	public void addExercises(List<Exercise> ex)
	{
		int currentSize = exercisesToInclude.size();
		List<RoutineExerciseHelper> routineExercisesToAdd = new ArrayList<>();
		for(Exercise e : ex){
			routineExercisesToAdd.add(new RoutineExerciseHelper(e));
		}
		exercisesToInclude.addAll(routineExercisesToAdd);
		notifyItemRangeInserted(currentSize + 1, ex.size());
	}
	
	public void removeExerciseAtPosition(int pos)
	{
		exercisesToInclude.remove(pos);
		notifyItemRemoved(pos);
	}
	
	// "Undo" the temporary delete of an exercise.
	public void restoreExercise(RoutineExerciseHelper ex, int position)
	{
		exercisesToInclude.add(position, ex);
		notifyItemInserted(position);
	}
	
	public void swap(int toPosition, int fromPosition)
	{
		Collections.swap(exercisesToInclude, toPosition, fromPosition);
		//notifyItemMoved(toPosition, fromPosition);
	}
}
// Inner Classes
