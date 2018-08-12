package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

import java.util.ArrayList;
import java.util.List;

public class StringArrayAdapter
	extends ArrayAdapter
{
	// This class will filter through the strings as the user types.
	private class ListFilter
		extends Filter
	{
		// Private
		private final Object lock = new Object();
		
		// Overrides
		@Override
		protected FilterResults performFiltering(CharSequence prefix)
		{
			FilterResults results = new FilterResults();
			if(dataListAllItems == null){
				synchronized(lock){
					dataListAllItems = new ArrayList<>(dataList);
				}
			}
			
			if(prefix == null || prefix.length() == 0){
				synchronized(lock){
					results.values = dataListAllItems;
					results.count = dataListAllItems.size();
				}
			}else{
				final String searchStrLowerCase = prefix.toString().toLowerCase();
				
				ArrayList<String> matchValues = new ArrayList<>();
				
				for(String dataItem : dataListAllItems){
					if(dataItem.toLowerCase().contains(searchStrLowerCase)){
						matchValues.add(dataItem);
					}
				}
				
				results.values = matchValues;
				results.count = matchValues.size();
			}
			
			return results;
		}
		
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			if(results.values != null){
				dataList = (ArrayList<String>)results.values;
			}else{
				dataList = null;
			}
			
			if(results.count > 0){
				notifyDataSetChanged();
			}else{
				notifyDataSetInvalidated();
			}
		}
	}
	
	private List<String> dataList;
	private Context mContext;
	private int itemLayout;
	private ListFilter listFilter = new ListFilter();
	private List<String> dataListAllItems;
	
	// Overrides
	
	public StringArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects)
	{
		super(context, resource, objects);
		
		dataList = objects;
		mContext = context;
		itemLayout = resource;
	}
	
	// Setters
	public void setList(List<String> items)
	{
		dataListAllItems = items;
	}
	@Override
	public int getCount()
	{
		return dataList.size();
	}
	
	@Override
	public String getItem(int position)
	{
		return dataList.get(position);
	}
	
	@Override
	public View getView(int position, View view, @NonNull ViewGroup parent)
	{
		if(view == null){
			view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
		}
		
		TextView strName = view.findViewById(R.id.textView);
		strName.setText(getItem(position));
		return view;
	}
	
	@NonNull
	@Override
	public Filter getFilter()
	{
		if(listFilter == null){
			listFilter = new ListFilter();
		}
		return listFilter;
	}
	
	// Check if the string is within the possible options.
	public boolean contains(String s)
	{
		// If Build is of high enough level, can do the search efficiently.
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			return dataList.stream().anyMatch(s::equalsIgnoreCase);
		}
		
		// Else, loop through entire list.
		for(String exerciseName : dataList){
			if(exerciseName.equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}
	// Inner Classes
}
// Inner Classes
