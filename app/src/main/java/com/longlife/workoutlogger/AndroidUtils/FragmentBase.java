package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FragmentBase
	extends Fragment
{
	private CompositeDisposable composite = new CompositeDisposable();
	protected FragmentNavigation fragmentNavigation;
	
	// Other
	// Overrides
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if(context instanceof FragmentNavigation){
			fragmentNavigation = (FragmentNavigation)context;
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		clearDisposables();
	}
	
	public void addDisposable(Disposable d)
	{
		composite.add(d);
	}
	
	public void clearDisposables()
	{
		composite.clear();
	}
	
	// Interface to communicate from fragment to activity, so activity can add the fragment to the navigation manager.
	public interface FragmentNavigation
	{
		void pushFragment(Fragment fragment);
	}
}
// Inner Classes
