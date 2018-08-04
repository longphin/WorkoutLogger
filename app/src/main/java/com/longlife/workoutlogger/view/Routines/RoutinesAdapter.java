package com.longlife.workoutlogger.view.Routines;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine;

import java.util.ArrayList;
import java.util.List;

public class RoutinesAdapter
	extends RecyclerView.Adapter<RoutinesViewHolder>
{
	// Static
	private static final String TAG = RoutinesAdapter.class.getSimpleName();
	private List<Routine> routines = new ArrayList<>();
	
	// [TODO] need to observe when a routine is added, and then notify about the insert. Would need to pass the viewModel to this adapter.
	
	public RoutinesAdapter()
	{
	}
	
	// Overrides
	@Override
	public void onBindViewHolder(RoutinesViewHolder holder, int pos)
	{
		int position = holder.getAdapterPosition();
		Routine routine = routines.get(position);
		
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(routine.getName())
			.append(" (")
			.append(routine.getIdRoutine())
			.append(")");
		
		holder.setNameText(sbName.toString());
		holder.setDescripText(routine.getDescription());
	}
	
	@Override
	public RoutinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
		
		return (new RoutinesViewHolder(v));
	}
	
	@Override
	public int getItemCount()
	{
		if(routines == null)
			return (0);
		return (routines.size());
	}
	
	// Setters
	public void setRoutines(List<Routine> routines)
	{
		if(routines == null)
			return;
		
		this.routines = routines;
		notifyDataSetChanged();
	}
	
	public void addRoutine(Routine routine)
	{
		Log.d(TAG, "inserted routine " + routine.getName() + " " + String.valueOf(routine.getIdRoutine()));
		if(routine == null)
			return;
		
		this.routines.add(routine);
		notifyItemInserted(routines.size() - 1);
	}
	
	public Routine getRoutine(int position)
	{
		return routines.get(position);
	}
	
	public void removeRoutine(int position)
	{
		routines.remove(position);
		notifyItemRemoved(position);
	}
	
	public void restoreRoutine(Routine restoredItem, int restoredPosition)
	{
		routines.add(restoredPosition, restoredItem);
		notifyItemInserted(restoredPosition);
	}
}
// Inner Classes
