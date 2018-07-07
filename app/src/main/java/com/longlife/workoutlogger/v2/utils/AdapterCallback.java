package com.longlife.workoutlogger.v2.utils;

public interface AdapterCallback
{
	void onItemClicked(int position);
	
	void onHeaderClick(int position);
	
	boolean isExpanded(int position);
}
