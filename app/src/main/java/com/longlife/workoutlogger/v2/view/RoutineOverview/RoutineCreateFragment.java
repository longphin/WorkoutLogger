package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.longlife.workoutlogger.v2.utils.BaseActivity;
import com.longlife.workoutlogger.v2.utils.FragmentWithCompositeDisposable;
import com.longlife.workoutlogger.v2.utils.Response;
import com.longlife.workoutlogger.v2.utils.StringArrayAdapter;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoutineCreateFragment extends FragmentWithCompositeDisposable {
    public static final String TAG = RoutineCreateFragment.class.getSimpleName();

    @Inject
    Context context;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private RoutinesOverviewViewModel viewModel;

    private RecyclerView recyclerView;
    private RoutineCreateAdapter adapter;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(context, "Selected " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Selected " + adapterView.getItemAtPosition(i));
        }
    };

    private View.OnClickListener onSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startSearchExercises();
        }
    };

    private AutoCompleteTextView searchBox;

    public RoutineCreateFragment() {
        // Required empty public constructor
    }

    public static RoutineCreateFragment newInstance() {
        return (new RoutineCreateFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(getActivity(), viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);

        addDisposable(viewModel.getLoadExercisesResponse().subscribe(response -> processLoadExercisesResponse(response)));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routine_create, container, false);

        TextView name = v.findViewById(R.id.edit_routineCreateName);
        TextView descrip = v.findViewById(R.id.edit_routineCreateDescrip);
        Button cancelButton = v.findViewById(R.id.btn_routineCreateCancel);
        Button saveButton = v.findViewById(R.id.btn_routineCreateSave);
        Button addExerciseToRoutine = v.findViewById(R.id.btn_addExerciseToRoutine);
        searchBox = v.findViewById(R.id.txt_routineexercisecreate_searchBox);
        searchBox.setOnItemClickListener(onItemClickListener);
        ImageView searchExercises = v.findViewById(R.id.btn_searchExercises);

        recyclerView = v.findViewById(R.id.rv_routineCreateExercises);
        initializeRecyclerView();

        // OnClick cancel button.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).onBackPressedCustom(view);
            }
        });

        // OnClick add exercise.
        addExerciseToRoutine.setOnClickListener(newView -> addExerciseToRoutine(newView)); //[TODO] remove this once all functions for adding exercise (autocomplete, search fragment, etc.) have been implemented

        // Search exercises image.
        searchExercises.setOnClickListener(onSearchClickListener);

        // Get exercises list.
        viewModel.loadExercises();

        return (v);
    }

    private void initializeRecyclerView() {
        //LimitedLinearLayoutManager layout = new LimitedLinearLayoutManager(context, 100);
        //recyclerView.setLayoutManager(layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new RoutineCreateAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        //viewModel.loadExercises(); // We don't need initial data.
    }

    public void addExerciseToRoutine(View v) {
        adapter.addExercise(new Exercise());
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        Log.d(TAG, String.valueOf(adapter.getItemCount()) + " exercises");
    }

    private void processLoadExercisesResponse(Response<List<Exercise>> response) {
        switch (response.getStatus()) {
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

    private void renderExercisesLoadingState() {
        Log.d(TAG, "loading exercises");
    }

    private void renderExercisesSuccessState(List<Exercise> exercises) {
        StringBuilder sb = new StringBuilder();
        sb.append(exercises == null ? 0 : exercises.size());
        sb.append(" exercises obtained");

        Log.d(TAG, sb.toString());

        List<String> tempStr = new ArrayList<>();
        for (Exercise e : exercises) {
            tempStr.add(e.getName());
        }
        StringArrayAdapter searchAdapter = new StringArrayAdapter(context, R.layout.autocompletetextview, tempStr);
        searchBox.setAdapter(searchAdapter);
    }

    private void renderExercisesErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        Log.d(TAG, throwable.getMessage());
    }

    // Click search exercise button
    public void startSearchExercises() {
        /*
        //AddExercisesFragment frag = new AddExercisesFragment();
        ExercisesOverviewFragment frag = new ExercisesOverviewFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_routines_overview, frag, AddExercisesFragment.TAG)
                .addToBackStack(null)
                .commit();
        */

        //
        FragmentManager manager = getActivity().getSupportFragmentManager();

        ExercisesOverviewFragment fragment = (ExercisesOverviewFragment) manager.findFragmentByTag(ExercisesOverviewFragment.TAG);
        if (fragment == null) {
            fragment = ExercisesOverviewFragment.newInstance();//(R.id.root_routines_overview);
            fragment.setRootId(R.id.root_routines_overview);

            /*
            manager.beginTransaction()
                    .replace(R.id.root_routines_overview, fragment, ExerciseCreateFragment.TAG)
                    .addToBackStack(null)
                    .commit();
            */
        }

        addFragmentToActivity(manager, fragment, R.id.root_routines_overview, ExercisesOverviewFragment.TAG, ExercisesOverviewFragment.TAG);
    }

    public void addFragmentToActivity(FragmentManager fragmentManager,
                                      Fragment fragment,
                                      int frameId,
                                      String tag,
                                      String addToBackStack) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        if (!addToBackStack.isEmpty())
            transaction.addToBackStack(addToBackStack);//(null);//transaction.addToBackStack(addToBackStack);
        transaction.commit();
    }
}
