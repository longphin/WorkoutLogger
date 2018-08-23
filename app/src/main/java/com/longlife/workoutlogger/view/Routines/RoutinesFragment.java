package com.longlife.workoutlogger.view.Routines;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateFragment;
import com.longlife.workoutlogger.view.Routines.Helper.DeletedRoutine;

import java.util.List;

import javax.inject.Inject;

public class RoutinesFragment
	extends FragmentBase
	implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
{
	public static final String TAG = RoutinesFragment.class.getSimpleName();
	private RoutinesViewModel viewModel;
	private RecyclerView recyclerView;
	private RoutinesAdapter adapter;
	private View mView;
	private ConstraintLayout viewRootLayout; // layout for recycler view
	@Inject
	public Context context;
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	
	public RoutinesFragment()
	{
	
	}
	
	// Overrides
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		((MyApplication)getActivity().getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);
		
		//viewModel.getLoadExercisesResponse().subscribe(response -> processLoadResponse(response));
		addDisposable(viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response)));
		addDisposable(viewModel.getInsertResponse().subscribe(response -> processInsertRoutineResponse(response)));
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		
		if(mView == null){
			mView = inflater.inflate(R.layout.fragment_routines, container, false);
			
			viewRootLayout = this.mView.findViewById(R.id.root_routines_layout);
			
			// Add listener to "add routine button"
			FloatingActionButton btn_addRoutine = mView.findViewById(R.id.btn_addRoutine);
			// Overrides
			btn_addRoutine.setOnClickListener(v -> {
				startCreateFragment();
			});
			
			// Initialize recyclerview.
			recyclerView = mView.findViewById(R.id.rv_routines);
			initializeRecyclerView();
		}
		
		return (mView);
	}
	
	@Override
	public boolean isItemViewSwipeEnabled()
	{
		return true;
	}
	
	@Override
	public boolean isLongPressDragEnabled()
	{
		return false;
	}
	
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos)
	{
		if(viewHolder instanceof RecyclerViewHolderSwipeable){
			int position = viewHolder.getAdapterPosition();
			final Routine deletedItem = adapter.getRoutine(position);
			
			Log.d(TAG, "Deleting... " + deletedItem.getName() + " at position " + String.valueOf(position));
			
			// Start the deleting process. It is only removed in the adapter, but it saved in the viewModel.
			// While the snackbar to undo the delete is available, the viewModel will keep the reference.
			// Upon the snackbar being dismissed, it will permanently remove the exercise.
			viewModel.addDeletedRoutine(deletedItem, position);
			adapter.removeRoutine(position);
			
			Snackbar snackbar = Snackbar
				.make(viewRootLayout, deletedItem.getName() + " deleted.", Snackbar.LENGTH_LONG);
			snackbar.setAction("UNDO", view -> {});
			snackbar.addCallback(new Snackbar.Callback()
			{
				// Overrides
				@Override
				public void onDismissed(Snackbar snackbar, int event)
				{
					final DeletedRoutine firstDeletedRoutine = viewModel.getFirstDeletedRoutine();
					if(firstDeletedRoutine == null)
						return;
					
					// If the snackbar was dismissed via clicking the action (Undo button), then restore the exercise.
					if(event == Snackbar.Callback.DISMISS_EVENT_ACTION){
						//adapter.restoreExercise(deletedItem, deletedIndex);
						adapter.restoreRoutine(firstDeletedRoutine.getRoutine(), firstDeletedRoutine.getPosition());
						return;
					}
					
					// For other dismiss events, permanently delete the exercise.
					Log.d(TAG, "Exercise deleted permanently: " + firstDeletedRoutine.getRoutine().getName() + " " + String.valueOf(firstDeletedRoutine.getRoutine().getIdRoutine()));
					viewModel.setRoutineHiddenStatus(firstDeletedRoutine.getRoutine().getIdRoutine(), true);
				}
			});
			snackbar.setActionTextColor(Color.YELLOW);
			snackbar.show();
		}
	}
	
	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
	{
		return false;
	}
	
	public static RoutinesFragment newInstance()
	{
		return (new RoutinesFragment());
	}
	
	// Methods
	private void processInsertRoutineResponse(Response<Routine> response)
	{
		switch(response.getStatus()){
			case LOADING:
				break;
			case SUCCESS:
				renderInsertSuccessState(response.getValue());
				break;
			case ERROR:
				break;
		}
	}
	
	private void renderInsertSuccessState(Routine routine)
	{
		adapter.addRoutine(routine);
	}
	
	private void initializeRecyclerView()
	{
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		adapter = new RoutinesAdapter();
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		
		ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
		new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
		
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
			.replace(R.id.frameLayout_main_activity,//R.id.root_routines_layout,
				fragment, RoutineCreateFragment.TAG
			)
			.addToBackStack(RoutineCreateFragment.TAG)
			.commit();
		
		int count = manager.getBackStackEntryCount();
		Log.d(TAG, "Number of activites in back stack: " + String.valueOf(count));
		for(int i = 0; i < count; i++){
			Log.d(TAG, "Backstack: " + manager.getBackStackEntryAt(i).getName());
		}
	}
}
// Inner Classes
