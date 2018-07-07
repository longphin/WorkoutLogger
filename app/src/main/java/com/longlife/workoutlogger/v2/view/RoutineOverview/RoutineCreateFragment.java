package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.AdapterCallback;
import com.longlife.workoutlogger.v2.utils.BaseActivity;
import com.longlife.workoutlogger.v2.utils.FragmentBase;
import com.longlife.workoutlogger.v2.utils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.v2.utils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.v2.utils.Response;
import com.longlife.workoutlogger.v2.utils.StringArrayAdapter;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewFragment;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewViewModel;
import com.longlife.workoutlogger.v2.view.RoutineOverview.AddSets.RoutineCreateExerciseSetFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoutineCreateFragment
	extends FragmentBase
	implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AdapterCallback
{
	public static final String TAG = RoutineCreateFragment.class.getSimpleName();
	private RoutinesOverviewViewModel routineViewModel;
	private ExercisesOverviewViewModel exerciseViewModel;
	private RecyclerView recyclerView;
	private RoutineCreateAdapter adapter;
	// OnClick listener for when item in recyclerview is clicked.
	private AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> Log.d(TAG, "Selected " + adapterView.getItemAtPosition(i));
	// OnClick listener for when "search exercise" image is clicked.
	private View.OnClickListener onSearchClickListener = view -> startSearchExercises();
	private AutoCompleteTextView searchBox;
	private ConstraintLayout coordinatorLayout; // layout for recycler view
	private View mView;
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	// Other
	@Inject
	Context context;
	
	public RoutineCreateFragment()
	{
		// Required empty public constructor
	}
	
	// Overrides
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		((MyApplication)getActivity().getApplication())
			.getApplicationComponent()
			.inject(this);
		
		routineViewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(getActivity(), viewModelFactory)
				.get(RoutinesOverviewViewModel.class);
		
		exerciseViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesOverviewViewModel.class);
		
		// Observer to get a list of all exercises. This list is used for the autocomplete searchbox to add an exercise by typing the name and it giving a hint for autocompleting the name.
		addDisposable(routineViewModel.getLoadExercisesResponse().subscribe(response -> processLoadExercisesResponse(response)));
		// Observer to get list of exercises to add to this routine through the ExercisesOverviewFragment.
		addDisposable(exerciseViewModel.getSelectedExercises().subscribe(response -> processSelectedExercisesResponse(response)));
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(R.layout.fragment_routine_create, container, false);
			
			TextView name = mView.findViewById(R.id.edit_routineCreateName);
			TextView descrip = mView.findViewById(R.id.edit_routineCreateDescrip);
			Button cancelButton = mView.findViewById(R.id.btn_routineCreateCancel);
			Button saveButton = mView.findViewById(R.id.btn_routineCreateSave);
			Button addExerciseToRoutine = mView.findViewById(R.id.btn_addExerciseToRoutine);
			searchBox = mView.findViewById(R.id.txt_routineexercisecreate_searchBox);
			searchBox.setOnItemClickListener(onItemClickListener);
			ImageView searchExercises = mView.findViewById(R.id.btn_searchExercises);
			coordinatorLayout = mView.findViewById(R.id.routine_create_layout);
			
			recyclerView = mView.findViewById(R.id.rv_routineCreateExercises);
			initializeRecyclerView();
			
			// OnClick cancel button.
			cancelButton.setOnClickListener(view -> ((BaseActivity)getActivity()).onBackPressedCustom(view));
			
			// OnClick add exercise.
			addExerciseToRoutine.setOnClickListener(newView -> addExerciseToRoutine(newView)); //[TODO] remove this once all functions for adding exercise (autocomplete, search fragment, etc.) have been implemented
			
			// Search exercises image.
			searchExercises.setOnClickListener(onSearchClickListener);
			
			// Get exercises list.
			routineViewModel.loadExercises();
		}
		
		return (mView);
	}
	
	// On Swipe for recyclerview item.
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos)
	{
		if(viewHolder instanceof RecyclerViewHolderSwipeable){
			int position = viewHolder.getAdapterPosition();
			
			if(adapter.isSwipeable(position))
				return;
			
			// get the removed item name to display it in snack bar
			List<RoutineExerciseHelper> exercises = adapter.getRoutineExercises();
			String name = exercises.get(position).getExercise().getName();
			
			// backup of removed item for undo purpose
			final RoutineExerciseHelper deletedItem = exercises.get(position);
			final int deletedIndex = position;
			
			// remove the item from recycler view
			adapter.removeExerciseAtPosition(position); // [TODO] this remove does not update the position?
			adapter.notifyItemRemoved(position);
			
			// showing snack bar with Undo option
			Snackbar snackbar = Snackbar
				.make(coordinatorLayout, name + " deleted.", Snackbar.LENGTH_LONG);
			snackbar.setAction("UNDO", view -> adapter.restoreExercise(deletedItem, deletedIndex));
			snackbar.setActionTextColor(Color.YELLOW);
			snackbar.show();
		}
	}
	
	// On drag up and down for recyclerview item.
	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
	{
		int fromPosition = viewHolder.getAdapterPosition();
		int toPosition = target.getAdapterPosition();
		
		if(fromPosition < adapter.getItemCount() && toPosition < adapter.getItemCount()){
			if(fromPosition < toPosition){
				for(int i = fromPosition; i < toPosition; i++){
					adapter.swap(i, i + 1);
				}
			}else{
				for(int i = fromPosition; i > toPosition; i--){
					adapter.swap(i, i - 1);
				}
			}
			adapter.notifyItemMoved(fromPosition, toPosition);
		}
		return true;
	}
	
	@Override
	public boolean isLongPressDragEnabled()
	{
		return true;
	}
	
	@Override
	public boolean isItemViewSwipeEnabled()
	{
		return true;
	}
	
	@Override
	public void onItemClicked(int position)
	{
		startExerciseSetFragment(position);
	}
	
	@Override
	public void onHeaderClick(int position)
	{
		adapter.onHeaderClick(position);
	}
	
	@Override
	public boolean isExpanded(int position)
	{
		return adapter.isExpanded(position);
	}
	
	public static RoutineCreateFragment newInstance()
	{
		return (new RoutineCreateFragment());
	}
	
	// Methods
	// Process list of exercises that were selected in the searchbox fragment.
	private void processSelectedExercisesResponse(Response<List<Exercise>> response)
	{
		switch(response.getStatus()){
			case LOADING:
				renderSelectedExercisesState();
				break;
			case SUCCESS:
				renderSelectedExercisesSuccessState(response.getValue());
				break;
			case ERROR:
				renderSelectedExercisesErrorState(response.getError());
				break;
		}
	}
	
	private void renderSelectedExercisesState()
	{
	}
	
	private void initializeRecyclerView()
	{
		//LimitedLinearLayoutManager layout = new LimitedLinearLayoutManager(context, 100);
		//recyclerView.setLayoutManager(layout);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		adapter = new RoutineCreateAdapter(getContext(), RoutineCreateFragment.this);
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		
		// Callback to detach swipe to delete motion.
		ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT, this);
		new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
		//routineViewModel.loadExercises(); // We don't need initial data.
	}
	
	private void renderSelectedExercisesErrorState(Throwable error)
	{
	}
	
	// Process list of all exercises used in autocomplete searchbox.
	private void processLoadExercisesResponse(Response<List<Exercise>> response)
	{
		switch(response.getStatus()){
			case LOADING:
				renderExercisesLoadingState();
				break;
			case SUCCESS:
				renderExercisesSuccessState(response.getValue());
				break;
			case ERROR:
				renderExercisesErrorState(response.getError());
				break;
		}
	}
	
	private void renderExercisesLoadingState()
	{
		Log.d(TAG, "loading exercises");
	}
	
	private void renderExercisesSuccessState(List<Exercise> exercises)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(exercises == null ? 0 : exercises.size());
		sb.append(" exercises obtained");
		
		Log.d(TAG, sb.toString());
		
		List<String> tempStr = new ArrayList<>();
		for(Exercise e : exercises){
			tempStr.add(e.getName());
		}
		StringArrayAdapter searchAdapter = new StringArrayAdapter(context, R.layout.autocompletetextview, tempStr);
		searchBox.setAdapter(searchAdapter);
	}
	
	private void renderExercisesErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
	}
	
	private void addExerciseToRoutine(View v)
	{
		adapter.addExercise(new Exercise());
		adapter.notifyItemInserted(adapter.getItemCount() - 1);
		Log.d(TAG, String.valueOf(adapter.getItemCount()) + " exercises");
	}
	
	// Click search exercise button
	private void startSearchExercises()
	{
		FragmentManager manager = getActivity().getSupportFragmentManager();
		
		ExercisesOverviewFragment fragment = (ExercisesOverviewFragment)manager.findFragmentByTag(ExercisesOverviewFragment.TAG);
		if(fragment == null){
			fragment = ExercisesOverviewFragment.newInstance(R.id.root_routines_overview, R.layout.item_exercises_selectable, R.layout.fragment_routine_exercises_overview);
			/*
			fragment.setRootId(R.id.root_routines_overview);
			fragment.setItemLayout(R.layout.item_exercises_selectable);
			fragment.setOverviewLayout(R.layout.fragment_routine_exercises_overview);
			*/
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_routines_overview, ExercisesOverviewFragment.TAG, ExercisesOverviewFragment.TAG);
	}
	
	private void addFragmentToActivity(FragmentManager fragmentManager,
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
	
	private void renderSelectedExercisesSuccessState(List<Exercise> ex)
	{
		Log.d(TAG, "Number of exercises added: " + String.valueOf(ex.size()));
		int countBefore = adapter.getItemCount();
		adapter.addExercises(ex);
		adapter.notifyItemRangeInserted(countBefore + 1, adapter.getItemCount());
	}
	
	// Click search exercise button
	private void startExerciseSetFragment(int position)
	{
		FragmentManager manager = getActivity().getSupportFragmentManager();
		RoutineCreateExerciseSetFragment fragment = (RoutineCreateExerciseSetFragment)manager.findFragmentByTag(RoutineCreateExerciseSetFragment.TAG);
		
		if(fragment == null){
			RoutineExerciseHelper exerciseToAddSetsTo = adapter.getRoutineExercises().get(position);
			fragment = RoutineCreateExerciseSetFragment.instance(exerciseToAddSetsTo.getExercise(), (ArrayList)exerciseToAddSetsTo.getSets());
		}
		addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutineCreateExerciseSetFragment.TAG, RoutineCreateExerciseSetFragment.TAG);
		/*
		FragmentManager manager = getActivity().getSupportFragmentManager();
		
		ExercisesOverviewFragment fragment = (ExercisesOverviewFragment)manager.findFragmentByTag(ExercisesOverviewFragment.TAG);
		if(fragment == null){
			fragment = ExercisesOverviewFragment.newInstance();
			fragment.setRootId(R.id.root_routines_overview);
			fragment.setItemLayout(R.layout.item_exercises_selectable);
			fragment.setOverviewLayout(R.layout.fragment_routine_exercises_overview);
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_routines_overview, ExercisesOverviewFragment.TAG, ExercisesOverviewFragment.TAG);
		*/
	}
}
// Inner Classes
