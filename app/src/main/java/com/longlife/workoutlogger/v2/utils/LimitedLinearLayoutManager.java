package com.longlife.workoutlogger.v2.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

public class LimitedLinearLayoutManager
	extends LinearLayoutManager
{
	private int maxHeight;
	
	public LimitedLinearLayoutManager(Context context, int maxHeight)
	{
		super(context);
		this.maxHeight = maxHeight;
	}
	
	public LimitedLinearLayoutManager(Context context, int orientation, boolean reverseLayout, int maxHeight)
	{
		super(context, orientation, reverseLayout);
		this.maxHeight = maxHeight;
	}
	
	public LimitedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int maxHeight)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		this.maxHeight = maxHeight;
	}
	
	// Overrides
	@Override
	public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthMeasureSpec, int heightMeasureSpec)
	{
		if(maxHeight > 0){
			int hSize = MeasureSpec.getSize(heightMeasureSpec);
			int hMode = MeasureSpec.getMode(heightMeasureSpec);
			
			switch(hMode){
				case MeasureSpec.AT_MOST:
					heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.AT_MOST);
					break;
				case MeasureSpec.UNSPECIFIED:
					heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
					break;
				case MeasureSpec.EXACTLY:
					heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.EXACTLY);
					break;
			}
		}
		
		super.onMeasure(recycler, state, widthMeasureSpec, heightMeasureSpec);
	}
}
// Inner Classes
