package com.longlife.workoutlogger.AndroidUtils;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.longlife.workoutlogger.R;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class ActivityBase
	extends AppCompatActivity
{
	// Static
	private static final String TAG = ActivityBase.class.getSimpleName();
	private CompositeDisposable composite = new CompositeDisposable();
	
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	public FragmentManager manager = getSupportFragmentManager();
	
	// Overrides
/*	@Override
	public void onBackPressed()
	{
		if(manager.getBackStackEntryCount() > 0){ // if current view is a fragment, then pop it.
			manager.popBackStack();
			hideKeyboard(this);
			
			int count = manager.getBackStackEntryCount();
			Log.d(TAG, "Number of activites in back stack: " + String.valueOf(count));
			for(int i = 0; i < count; i++){
				Log.d(TAG, "Backstack: " + manager.getBackStackEntryAt(i).getName());
			}
		}else{ // else, we are are at the activity level, so we call the super onBackPressed();
			super.onBackPressed();
		}
	}*/
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		clearDisposables();
	}
	
	public static void hideKeyboard(Activity activity)
	{
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = activity.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if(view == null){
			view = new View(activity);
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public void addDisposable(Disposable d)
	{
		composite.add(d);
	}
	
	public void clearDisposables()
	{
		composite.clear();
	}
	
	public void onBackPressedCustom(View view)
	{
		onBackPressed();
		if(view != null){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			view.clearFocus();
		}
	}
	
	public void addFragmentToActivity(FragmentManager fragmentManager,
		Fragment fragment,
		int frameId,
		String tag,
		String addToBackStack)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(frameId, fragment, tag);
		if(!addToBackStack.isEmpty())
			transaction.addToBackStack(addToBackStack);//(null);
		transaction.commit();
		
		int count = manager.getBackStackEntryCount();
		Log.d(TAG, "Number of activites in back stack: " + String.valueOf(count));
		for(int i = 0; i < count; i++){
			Log.d(TAG, "Backstack: " + manager.getBackStackEntryAt(i).getName());
		}
	}
	
	public void initToolbar(Toolbar toolbar, boolean isBackEnabled)
	{
		setSupportActionBar(toolbar);
		
		if(isBackEnabled){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_down_black_24dp);
			
		}
	}
	
	public void initToolbar(Toolbar toolbar, String title, boolean isBackEnabled)
	{
		
		setSupportActionBar(toolbar);
		
		if(isBackEnabled){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_down_black_24dp);
			
		}
		
		getSupportActionBar().setTitle(title);
	}
}
// This extends AppCompatActivity and allows us to easily attach an activity to its fragment.
// Inner Classes
