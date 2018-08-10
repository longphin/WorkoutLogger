package com.longlife.workoutlogger.view.Routines.CreateRoutine;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.AndroidUtils.StringArrayAdapter;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.DialogFragment.AddNoteDialog;
import com.longlife.workoutlogger.view.Exercises.EditExercise.ExerciseEditFragment;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableAdapter;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableFragment;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableViewModel;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoutineCreateFragment
	extends FragmentBase
	implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
						 AddNoteDialog.OnInputListener,
						 ExercisesSelectableAdapter.IClickExercise
{
	public static final String TAG = RoutineCreateFragment.class.getSimpleName();
	private RoutinesViewModel routineViewModel;
	private ExercisesSelectableViewModel exerciseViewModel;
	private RecyclerView recyclerView;
	private RoutineCreateAdapter adapter;
	// OnClick listener for when item in recyclerview is clicked.
	private AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> Log.d(TAG, "Selected " + adapterView.getItemAtPosition(i));
	// OnClick listener for when "search exercise" image is clicked.
	private View.OnClickListener onSearchClickListener = view -> startSearchExercises();
	private AutoCompleteTextView searchBox;
	private ConstraintLayout coordinatorLayout; // layout for recycler view
	private View mView;
	private EditText name;
	private ImageView addNoteImage;
	
	// This class is used to validate the inputs.
	private class FreeFormSearchExercisesValidator
		implements AutoCompleteTextView.Validator
	{
		// Overrides
		@Override
		public boolean isValid(CharSequence charSequence)
		{
			return searchAdapter.contains(charSequence.toString());
		}
		
		@Override
		public CharSequence fixText(CharSequence charSequence)
		{
			return null;
		}
	}
	
	private ImageView searchBoxStatusImage;
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	// Adapter for free form exercise search.
	private StringArrayAdapter searchAdapter;
	String descrip;
	@Inject
	Context context;
	
	public RoutineCreateFragment()
	{
		// Required empty public constructor
	}
	
	// Other
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		((MyApplication)getActivity().getApplication())
			.getApplicationComponent()
			.inject(this);
		
		routineViewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(getActivity(), viewModelFactory)
				.get(RoutinesViewModel.class);
		
		exerciseViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesSelectableViewModel.class);
		
		// Observer to get a list of all exercises. This list is used for the autocomplete searchbox to add an exercise by typing the name and it giving a hint for autocompleting the name.
		addDisposable(routineViewModel.getLoadExercisesResponse().subscribe(response -> processLoadExercisesResponse(response)));
		// Observer to get list of exercises to add to this routine through the ExercisesOverviewFragment.
		addDisposable(exerciseViewModel.getAddExercisesToRoutineResponse().subscribe(response -> processSelectedExercisesResponse(response)));
		// Observer to get when routine is successfully saved.
		addDisposable(routineViewModel.getInsertResponse().subscribe(response -> processInsertResponse(response)));
	}
	// Overrides
	
	// On Swipe for recyclerview item.
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos)
	{
		if(viewHolder instanceof RecyclerViewHolderSwipeable){
			int position = viewHolder.getAdapterPosition();
			
			int swipedItemType = adapter.getItemViewType(position);
			
			if(swipedItemType == RoutineCreateAdapter.getHeaderTypeEnum()){
				// get the removed item name to display it in snack bar
				
				// backup of removed item for undo purpose
				final int deletedIndex = adapter.getHeaderIndex(position);
				final RoutineExerciseHelper deletedItem = adapter.getHeaderAtPosition(position);
				//final int deletedIndex = position;
				String name = deletedItem.getExercise().getName();
				
				// remove the item from recycler view
				adapter.removeExerciseAtPosition(position);
				
				// showing snack bar with Undo option
				Snackbar snackbar = Snackbar
					.make(coordinatorLayout, name + " deleted.", Snackbar.LENGTH_LONG);
				snackbar.setAction("UNDO", view -> adapter.restoreExercise(deletedItem, deletedIndex));
				snackbar.setActionTextColor(Color.YELLOW);
				snackbar.show();
			}else{
				adapter.removeItemAtPosition(position);
			}
		}
	}
	
	@Override
	public void sendInput(String descrip)
	{
		this.descrip = descrip;
	}
	
	// On drag up and down for recyclerview item.
	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
	{
		int fromPosition = viewHolder.getAdapterPosition();
		int toPosition = target.getAdapterPosition();
		
		if(fromPosition < adapter.getItemCount() && toPosition < adapter.getItemCount()){
			
			boolean swapWasDone = false;
			
			if(fromPosition < toPosition){
				for(int i = fromPosition; i < toPosition; i++){
					swapWasDone = swapWasDone || adapter.swap(i, i + 1);
				}
			}else{
				for(int i = fromPosition; i > toPosition; i--){
					swapWasDone = swapWasDone || adapter.swap(i, i - 1);
				}
			}
			if(swapWasDone)
				adapter.notifyItemMoved(fromPosition, toPosition);
		}
		return true;
	}
	
	@Override
	public boolean isLongPressDragEnabled()
	{
		return false;
	}
	// Insertion Response
	
	@Override
	public boolean isItemViewSwipeEnabled()
	{
		return true;
	}
	
	public static RoutineCreateFragment newInstance()
	{
		return (new RoutineCreateFragment());
	}
	
	// When exercise is clicked, open up edit fragment
	@Override
	public void exerciseClicked(Long idExercise)
	{
		FragmentManager manager = getActivity().getSupportFragmentManager();
		
		ExerciseEditFragment fragment = (ExerciseEditFragment)manager.findFragmentByTag(ExerciseEditFragment.TAG);
		if(fragment == null){
			fragment = ExerciseEditFragment.newInstance(idExercise);
		}
		
		Log.d(TAG, "Editing exercise " + String.valueOf(idExercise));
		addFragmentToActivity(manager, fragment, R.id.root_routines, ExerciseEditFragment.TAG, ExerciseEditFragment.TAG);
	}
	private void renderInsertLoadingState()
	{
		if(isAdded())
			Log.d(TAG, "attached: loading exercises");
		else
			Log.d(TAG, "detached: loading exercises");
	}
	
	private void processInsertResponse(Response<Routine> response)
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
	
	private void renderInsertErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
	}
	
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
	
	private void renderInsertSuccessState(Routine routine)
	{
		if(isAdded()){
			Log.d(TAG, "attached: " + routine.getName());
			
			getActivity().onBackPressed();
		}else{
			Log.d(TAG, "detached: " + routine.getName());
		}
		
		//adapter.setRoutines(routineViewModel.getCachedRoutines()); //[TODO] need to set the added exercise helper that was added.
		//adapter.notifyItemRangeChanged(val, adapter.getItemCount());//viewModel.getCachedExercises().size());
		
		clearDisposables();
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
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(R.layout.fragment_routine_create, container, false);
			
			this.name = mView.findViewById(R.id.edit_routineCreateName);
			this.addNoteImage = mView.findViewById(R.id.imv_routine_create_addNote);
			Button cancelButton = mView.findViewById(R.id.btn_routineCreateCancel);
			Button saveButton = mView.findViewById(R.id.btn_routineCreateSave);
			searchBox = mView.findViewById(R.id.txt_routineexercisecreate_searchBox);
			searchBoxStatusImage = mView.findViewById(R.id.imv_routine_create_searchBoxStatus);
			ImageView searchExercises = mView.findViewById(R.id.btn_searchExercises);
			coordinatorLayout = mView.findViewById(R.id.routine_create_layout);
			
			recyclerView = mView.findViewById(R.id.rv_routineCreateExercises);
			// Initialize recycler view
			initializeRecyclerView();
			
			// OnClick cancel button.
			cancelButton.setOnClickListener(view -> ((ActivityBase)getActivity()).onBackPressedCustom(view));
			
			// OnClick listener to change the routine name and description. Opens up a dialog fragment for user to change the values.
			this.addNoteImage.setOnClickListener(view ->
			{
				AddNoteDialog dialog = AddNoteDialog.newInstance(this.descrip);
				dialog.show(getChildFragmentManager(), AddNoteDialog.TAG);
			});
			
			// OnClick for saving routine.
			saveButton.setOnClickListener(view ->
			{
				if(!this.name.getText().equals("")){
					Routine routineToAdd = new Routine();
					routineToAdd.setName(name.getText().toString());
					routineToAdd.setDescription(descrip);//descrip.getText().toString());
					routineViewModel.insertRoutineFull(routineToAdd, adapter.getRoutineExerciseHelpers());
					
					getActivity().onBackPressed();
				}else{
					Log.d(TAG, "Routine needs a name"); // [TODO] pop up a message that says it needs a name.
				}
			});
			
			// Search exercises image.
			searchExercises.setOnClickListener(onSearchClickListener);
			// Search icon: When selected, search through entire exercise list.
			searchBox.setOnItemClickListener(onItemClickListener);
			
			// Get exercises list.
			routineViewModel.loadExercises();
		}
		
		return (mView);
	}
	
	// [TODO] add ability to set rest time for sets.
	
	// Methods
	
	private void renderExercisesErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
	}
	private void renderExercisesSuccessState(List<Exercise> exercises)
	{
		Log.d(TAG, String.valueOf(exercises == null ? 0 : exercises.size()) + " exercises obtained");
		
		if(exercises == null)
			return;
		
		List<String> tempStr = new ArrayList<>();
		for(Exercise e : exercises){
			tempStr.add(e.getName());
		}
		searchAdapter = new StringArrayAdapter(context, R.layout.autocompletetextview, tempStr);
		searchBox.setAdapter(searchAdapter);
		// Add a TextWatcher to the search box to determine if the search has a match.
		searchBox.addTextChangedListener(new TextWatcher()
		{
			// Other
			// Add handler and runnable to give a delay on the text check.
			Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
			Runnable workRunnable;
			
			// Overrides
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
			
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
					searchBoxStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings_ethernet_black_24dp, context.getTheme()));
				}else{
					searchBoxStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings_ethernet_black_24dp));
				}
				
				handler.removeCallbacks(workRunnable);
				workRunnable = () -> {
					if(searchAdapter.contains(charSequence.toString())){
						Log.d(TAG, charSequence.toString() + " is in list");
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
							searchBoxStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp, context.getTheme()));
						}else{
							searchBoxStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
						}
					}else{
						Log.d(TAG, charSequence.toString() + " not in list");
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
							searchBoxStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_error_outline_black_24dp, context.getTheme()));
						}else{
							searchBoxStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_error_outline_black_24dp));
						}
					}
				};
				
				handler.postDelayed(workRunnable, 500);
			}
			
			@Override
			public void afterTextChanged(Editable editable)
			{
			
			}
		});
		// [TODO] listen to the inputs and call (AutoCompleteTextView)searchBox.performValidation().
		// See https://proandroiddev.com/building-an-autocompleting-edittext-using-rxjava-f69c5c3f5a40
		// and https://stackoverflow.com/questions/5033246/android-autocomplettextview-force-text-to-be-from-the-entry-list
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
	
	private void initializeRecyclerView()
	{
		//LimitedLinearLayoutManager layout = new LimitedLinearLayoutManager(context, 100);
		//recyclerView.setLayoutManager(layout);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		adapter = new RoutineCreateAdapter(getContext());
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		
		// Callback to detach swipe to delete motion.
		ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT, this);
		new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
		//routineViewModel.loadExercises(); // We don't need initial data.
	}
	
	private void renderSelectedExercisesSuccessState(List<Exercise> ex)
	{
		adapter.addExercises(ex);
	}
	
	// Click search exercise button
	private void startSearchExercises()
	{
		FragmentManager manager = getActivity().getSupportFragmentManager();
		
		ExercisesSelectableFragment fragment = (ExercisesSelectableFragment)manager.findFragmentByTag(ExercisesSelectableFragment.TAG);
		if(fragment == null){
			//fragment = ExercisesOverviewFragment.newInstance(R.id.root_routines_overview, R.layout.item_exercise_selectable, R.layout.fragment_routine_exercises);
			fragment = new ExercisesSelectableFragment();
			fragment.setRootId(R.id.root_routines);
			fragment.setAdapter(new ExercisesSelectableAdapter(exerciseViewModel, this));
			fragment.setLayoutId(R.layout.fragment_routine_exercises);
			fragment.setViewModel(exerciseViewModel);
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_routines, ExercisesSelectableFragment.TAG, ExercisesSelectableFragment.TAG);
	}
}
// Inner Classes
