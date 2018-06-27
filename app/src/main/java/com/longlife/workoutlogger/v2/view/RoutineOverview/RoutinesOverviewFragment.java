package com.longlife.workoutlogger.v2.view.RoutineOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.utils.FragmentBase;
import com.longlife.workoutlogger.v2.utils.Response;

import java.util.List;

import javax.inject.Inject;

public class RoutinesOverviewFragment
	extends FragmentBase
{
	public static final String TAG = RoutinesOverviewFragment.class.getSimpleName();
	private RoutinesOverviewViewModel viewModel;
	private RecyclerView recyclerView;
	private RoutinesAdapter adapter;
	private View mView;
	@Inject
	public Context context;
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	
	public RoutinesOverviewFragment()
	{
	
	}
	
	public static RoutinesOverviewFragment newInstance()
	{
		return (new RoutinesOverviewFragment());
	}
	
	// Methods
	private void initializeRecyclerView()
	{
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		adapter = new RoutinesAdapter();
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		
		viewModel.loadRoutines();
	}
	
	private void processLoadResponse(Response<List<Routine>> response)
	{
		switch(response.getStatus()){
			case LOADING:
				renderLoadingState();
				break;
			case SUCCESS:
				renderSuccessState(response.getValue());
				break;
			case ERROR:
				renderErrorState(response.getError());
				break;
		}
	}
	
	private void renderLoadingState()
	{
		Toast.makeText(context, "loading routines", Toast.LENGTH_SHORT);
		
		Log.d(TAG, "loading routines");
	}
	
	private void renderSuccessState(List<Routine> routines)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(routines == null ? 0 : routines.size());
		sb.append(" routines obtained");
		
		Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT);
		
		Log.d(TAG, sb.toString());
		
		adapter.setRoutines(routines);
		adapter.notifyDataSetChanged();
	}
	
	private void renderErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT);
		Log.d(TAG, throwable.getMessage());
	}
	
	private void startCreateFragment()
	{
		FragmentManager manager = getActivity().getSupportFragmentManager();
		RoutineCreateFragment fragment = (RoutineCreateFragment)manager.findFragmentByTag(RoutineCreateFragment.TAG);
		if(fragment == null){
			fragment = RoutineCreateFragment.newInstance();
		}
		
		manager.beginTransaction()
			.replace(R.id.root_routines_overview, fragment, RoutineCreateFragment.TAG)
			.addToBackStack(RoutineCreateFragment.TAG)
			.commit();
		
		Log.d(TAG, "start routine create fragment");
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		((MyApplication)getActivity().getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(getActivity(), viewModelFactory)
				.get(RoutinesOverviewViewModel.class);
		
		//viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response));
		addDisposable(viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response)));
	}
	
	// Overrides
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		
		if(mView == null){
			View v = inflater.inflate(R.layout.fragment_routines_overview, container, false);
			
			// Add listener to "add routine button"
			FloatingActionButton btn_addRoutine = v.findViewById(R.id.btn_addRoutine);
			btn_addRoutine.setOnClickListener(new View.OnClickListener()
			{
				// Overrides
				@Override
				public void onClick(View v)
				{
					startCreateFragment();
					//viewModel.startCreateFragment();
				}
			});
			
			// Initialize recyclerview.
			recyclerView = v.findViewById(R.id.rv_routinesOverview);
			initializeRecyclerView();
			
			mView = v;
		}
		
		return (mView);
	}
}
// Inner Classes
