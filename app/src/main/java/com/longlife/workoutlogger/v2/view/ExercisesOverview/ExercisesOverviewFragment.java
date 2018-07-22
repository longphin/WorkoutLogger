package com.longlife.workoutlogger.v2.view.ExercisesOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Button;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.FragmentBase;
import com.longlife.workoutlogger.v2.utils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.v2.utils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.v2.utils.Response;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesOverviewFragment
	extends FragmentBase
	implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
{
	public static final String TAG = ExercisesOverviewFragment.class.getSimpleName();
	// Private
	private ExercisesOverviewViewModel viewModel;
	private RecyclerView recyclerView;
	private ConstraintLayout coordinatorLayout; // layout for recycler view
	private ExercisesAdapter adapter;
	private View mView;
	private int rootId;
	private int itemLayout;
	private int overviewLayout;
	private Button addExercisesToRoutine;
	// Public
	@Inject
	public Context context;
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	
	// Constructors
    /*
    public static final String rootId_TAG = "rootId";
    private int rootId;
    */
	public ExercisesOverviewFragment()
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
		
		rootId = getArguments().getInt(TAG + "rootId");
		itemLayout = getArguments().getInt(TAG + "itemLayout");
		overviewLayout = getArguments().getInt(TAG + "overviewLayout");
		initializeViewModel();
		
		// Observe events when the list of exercises is obtained.
		addDisposable(viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response)));
		addDisposable(viewModel.getInsertResponse().subscribe(response -> processInsertResponse(response)));
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(overviewLayout, container, false);
			
			// Add listener to when the "add exercises to routine" button is selected
			addExercisesToRoutine = mView.findViewById(R.id.btn_addExercisesToRoutine);
			if(addExercisesToRoutine != null){
				addExercisesToRoutine.setOnClickListener(
					view ->
					{
						viewModel.addExercisesToRoutine(adapter.getSelectedIdExercisesList());
						getActivity().onBackPressed();
					}
				);
			}
			
			// Add listener to "add routine button"
			FloatingActionButton btn_addRoutine = mView.findViewById(R.id.btn_addExercise);
			btn_addRoutine.setOnClickListener(view -> startCreateFragment());
			
			// Initialize recyclerview.
			recyclerView = mView.findViewById(R.id.rv_exercisesOverview);
			coordinatorLayout = mView.findViewById(R.id.exercises_overview_layout);
			initializeRecyclerView();
		}
		return mView;
	}
	
	// On Swipe for recycler view item.
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos)
	{
		if(viewHolder instanceof RecyclerViewHolderSwipeable){
			int position = viewHolder.getAdapterPosition();
			
			// get the removed item name to display it in snack bar
			List<Exercise> exercises = viewModel.getCachedExercises();
			String name = exercises.get(position).getName();
			
			// backup of removed item for undo purpose
			final Exercise deletedItem = exercises.get(position);
			final int deletedIndex = position;
			
			// remove the item from recycler view
			adapter.removeExercise(position);
			
			// showing snack bar with Undo option
			Snackbar snackbar = Snackbar
				.make(coordinatorLayout, name + " deleted.", Snackbar.LENGTH_LONG);
			snackbar.setAction("UNDO", view -> adapter.restoreExercise(deletedItem, deletedIndex));
			snackbar.addCallback(new Snackbar.Callback()
			{
				// Overrides
				@Override
				public void onDismissed(Snackbar snackbar, int event)
				{
					// If the snackbar was dismissed via clicking the action (Undo button), then do not permanently delete the exercise.
					if(event == Snackbar.Callback.DISMISS_EVENT_ACTION)
						return;
					
					// For other dismiss events, permanently delete the exercise.
					Log.d(TAG, "Exercise deleted permanently. " + String.valueOf(deletedItem.getIdExercise()));
					adapter.permanentlyRemoveExercise(deletedItem.getIdExercise());
					viewModel.deleteExercise(deletedItem);
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
	
	@Override
	public boolean isLongPressDragEnabled()
	{
		return false;
	}
	
	@Override
	public boolean isItemViewSwipeEnabled()
	{
		return true;
	}
	
	// Setters
	
	// Static methods
	public static ExercisesOverviewFragment newInstance(int i1, int i2, int i3)
	{
		ExercisesOverviewFragment fragment = new ExercisesOverviewFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(TAG + "rootId", i1);
		bundle.putInt(TAG + "itemLayout", i2);
		bundle.putInt(TAG + "overviewLayout", i3);
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	// Methods
	private void startCreateFragment()
	{
		FragmentManager manager = getActivity().getSupportFragmentManager();
		
		ExerciseCreateFragment fragment = (ExerciseCreateFragment)manager.findFragmentByTag(ExerciseCreateFragment.TAG);
		if(fragment == null){
			fragment = ExerciseCreateFragment.newInstance();
		}
		
		manager.beginTransaction()
			.replace(rootId, fragment, ExerciseCreateFragment.TAG)
			.addToBackStack(ExerciseCreateFragment.TAG)
			.commit();
	}
	
	private void initializeRecyclerView()
	{
		initializeViewModel(); // creates view model if the onCreateView() was called before onCreate()
		
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		adapter = new ExercisesAdapter(viewModel, itemLayout);
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		
		// Callback to detach swipe to delete motion.
		ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
		new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
		
		// populate recycler view with all data
		viewModel.loadExercises();
	}
	
	private void initializeViewModel()
	{
		if(viewModel == null){
			viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
				ViewModelProviders.of(getActivity(), viewModelFactory)
					.get(ExercisesOverviewViewModel.class);
		}
	}
	
	///
	/// GET EXERCISES RENDERING
	///
	private void processLoadResponse(Response<List<Exercise>> response)
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
	
	// Insertion Response
	private void processInsertResponse(Response<Integer> response)
	{
		switch(response.getStatus()){
			case LOADING:
				renderInsertLoadingState();
				break;
			case SUCCESS:
				renderInsertSuccessState(response.getValue());
				break;
			case ERROR:
				renderInsertErrorState(response.getError());
				break;
		}
	}
	
	private void renderLoadingState()
	{
		Log.d(TAG, "loading exercises");
	}
	
	private void renderSuccessState(List<Exercise> exercises)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(exercises == null ? 0 : exercises.size());
		sb.append(" exercises obtained");
		
		Log.d(TAG, sb.toString());
		
		adapter.setExercises(exercises);
	}
	
	private void renderErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
	}
	
	private void renderInsertLoadingState()
	{
		if(isAdded())
			Log.d(TAG, "attached: loading exercises");
		else
			Log.d(TAG, "detached: loading exercises");
	}
	
	private void renderInsertSuccessState(Integer val)
	{
		if(isAdded())
			Log.d(TAG, "attached: " + val.toString());
		else
			Log.d(TAG, "detached: " + val.toString());
		
		adapter.setExercises(viewModel.getCachedExercises());
	}
	
	private void renderInsertErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
	}
}

// Inner Classes
