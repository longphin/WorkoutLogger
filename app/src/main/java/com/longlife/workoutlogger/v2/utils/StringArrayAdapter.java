package com.longlife.workoutlogger.v2.utils;

import android.content.Context;
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
	private class ListFilter
		extends Filter
	{
		private Object lock = new Object();
		
		// Overrides
		@Override
		protected FilterResults performFiltering(CharSequence prefix)
		{
			FilterResults results = new FilterResults();
			if(dataListAllItems == null){
				synchronized(lock){
					dataListAllItems = new ArrayList<String>(dataList);
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
	
	public StringArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects)
	{
		super(context, resource, objects);
		
		dataList = objects;
		mContext = context;
		itemLayout = resource;
	}
	
	// Overrides
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
	// Inner Classes
}
// Inner Classes
