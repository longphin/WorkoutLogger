package com.longlife.workoutlogger.view.Exercises;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesFragment
	extends FragmentBase
	implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
{
	public static final String TAG = ExercisesFragment.class.getSimpleName();
	@Required
	private int rootId; //This is the root of the layout from the parent activity. This is needed to determine how to attach the child ExercisesCreateFragment when opened to create a new exercise.
	private ExercisesViewModel viewModel;
	@Required
	private int layoutId;
	
	private RecyclerView recyclerView;
	private ConstraintLayout viewRootLayout; // layout for recycler view
	
	@Inject
	public Context context;
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	protected View mView;
	protected ExercisesAdapter adapter;
	
	// Overrides
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		((MyApplication)getActivity().getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(getActivity(), viewModelFactory)
				.get(ExercisesViewModel.class);
		
		addDisposable(viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response)));
		addDisposable(viewModel.getExerciseInsertedResponse().subscribe(response -> processInsertExerciseResponse(response)));
		
		Log.d(TAG, "OnCreate: loadExercises()");
		viewModel.loadExercises();
	}
	
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos)
	{
		if(viewHolder instanceof RecyclerViewHolderSwipeable){
			int position = viewHolder.getAdapterPosition();
			final Exercise deletedItem = adapter.getExercise(position);
			
			Log.d(TAG, "Deleting... " + deletedItem.getName() + " at position " + String.valueOf(position));
			
			// Start the deleting process. It is only removed in the adapter, but it saved in the viewModel.
			// While the snackbar to undo the delete is available, the viewModel will keep the reference.
			// Upon the snackbar being dismissed, it will permanently remove the exercise.
			viewModel.addDeletedExercise(deletedItem, position);
			adapter.removeExercise(position);
			
			Snackbar snackbar = Snackbar
				.make(viewRootLayout, deletedItem.getName() + " deleted.", Snackbar.LENGTH_LONG);
			snackbar.setAction("UNDO", view -> {});
			snackbar.addCallback(new Snackbar.Callback()
			{
				// Overrides
				@Override
				public void onDismissed(Snackbar snackbar, int event)
				{
					final DeletedExercise firstDeletedExercise = viewModel.getFirstDeletedExercise();
					if(firstDeletedExercise == null)
						return;
					
					// If the snackbar was dismissed via clicking the action (Undo button), then restore the exercise.
					if(event == Snackbar.Callback.DISMISS_EVENT_ACTION){
						//adapter.restoreExercise(deletedItem, deletedIndex);
						adapter.restoreExercise(firstDeletedExercise.getExercise(), firstDeletedExercise.getPosition());
						return;
					}
					
					// For other dismiss events, permanently delete the exercise.
					Log.d(TAG, "Exercise deleted permanently: " + firstDeletedExercise.getExercise().getName() + " " + String.valueOf(firstDeletedExercise.getExercise().getIdExercise()));
					viewModel.setExerciseHiddenStatus(firstDeletedExercise.getExercise().getIdExercise(), true);
				}
			});
			snackbar.setActionTextColor(Color.YELLOW);
			snackbar.show();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(layoutId, container, false);
			
			recyclerView = mView.findViewById(R.id.rv_exercises);
			viewRootLayout = mView.findViewById(R.id.exercises_overview_layout);
			
			FloatingActionButton btn_addRoutine = mView.findViewById(R.id.btn_addExercise);
			btn_addRoutine.setOnClickListener(view -> startCreateFragment());
			
			initializeRecyclerView();
		}
		// Inflate the layout for this fragment
		return mView;
	}
	
	// Setters
	// @Required - This is the id of the parent activity/fragment's layout root.
	public void setRootId(int rootId)
	{
		this.rootId = rootId;
	}
	
	public void setLayoutId(int layoutId){this.layoutId = layoutId;}
	
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
	
	public void setAdapter(ExercisesAdapter adapter)
	{
		this.adapter = adapter;
	}
	
	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
	{
		return false;
	}
	
	// Methods
	private void processInsertExerciseResponse(Response<Exercise> response)
	{
		switch(response.getStatus()){
			case LOADING:
				break;
			case SUCCESS:
				renderInsertExerciseSuccess(response.getValue());
				break;
			case ERROR:
				break;
		}
	}
	
	private void renderInsertExerciseSuccess(Exercise ex)
	{
		adapter.addExercise(ex);
	}
	
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
				if(response.getError() != null)
					renderErrorState(response.getError());
				break;
		}
	}
	
	private void renderErrorState(@NonNull Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
	}
	
	public void initializeRecyclerView()
	{
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		//adapter = new ExercisesAdapter();
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		
		ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
		new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
	}
	
	private void renderLoadingState()
	{
		Log.d(TAG, "loading exercises");
	}
	
	private void renderSuccessState(List<Exercise> exercises)
	{
		StringBuilder sb = new StringBuilder();
		if(isAdded())
			sb.append("attached) ");
		else
			sb.append("detached) ");
		sb.append(exercises == null ? 0 : exercises.size());
		sb.append(" exercises obtained");
		
		Log.d(TAG, sb.toString());
		
		if(exercises == null)
			return;
		
		adapter.setExercises(exercises);
	}
	
	protected void startCreateFragment()
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
	
}
