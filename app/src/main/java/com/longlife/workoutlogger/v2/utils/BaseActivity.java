package com.longlife.workoutlogger.v2.utils;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

// Inner Classes
// This extends AppCompatActivity and allows us to easily attach an activity to its fragment.
public abstract class BaseActivity
				extends AppCompatActivity
{
	// Static
	private static final String TAG = "BaseActivity";
	private CompositeDisposable composite = new CompositeDisposable();

	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	public FragmentManager manager = getSupportFragmentManager();

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
	                                  String tag)
	{
		addFragmentToActivity(fragmentManager, fragment, frameId, tag, "");
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
	}

	// Overrides
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		clearDisposables();
	}

	@Override
	public void onBackPressed()
	{
		if(manager.getBackStackEntryCount() > 0){ // if current view is a fragment, then pop it.
			manager.popBackStack();

			int count = manager.getBackStackEntryCount();
			Log.d(TAG, "Number of activites in back stack: " + String.valueOf(count));
			for(int i = 0; i < count; i++){
				Log.d(TAG, "Backstack: " + manager.getBackStackEntryAt(i).getName());
			}
		}else{ // else, we are are at the activity level, so we call the super onBackPressed();
			super.onBackPressed();
		}
        /*
        super.onBackPressed();
        overridePendingTransition(0, 0);
        */
	}
}
