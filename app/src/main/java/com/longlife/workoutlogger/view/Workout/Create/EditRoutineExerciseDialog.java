/*
 * Created by Longphi Nguyen on 3/24/19 3:06 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/24/19 3:06 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine.ExerciseSet;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class EditRoutineExerciseDialog extends DialogBase {
    public static final String TAG = EditRoutineExerciseDialog.class.getSimpleName();
    private static final String INPUT_IDROUTINEEXERCISE = "idRoutineExercise";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    // private RoutinesViewModel routineViewModel;
    private Long idRoutineExercise;
    private RecyclerView setsRecyclerView;
    private ExerciseSetsAdapter adapter;
    private List<ExerciseSet> setsData_temporary = new ArrayList<>();
    private onSetDialogInteraction callback;
    private boolean dataWasInitialized = false;

    public static EditRoutineExerciseDialog newInstance(Long idRoutineExercise) {
        Bundle bundle = new Bundle();
        bundle.putLong(INPUT_IDROUTINEEXERCISE, idRoutineExercise);

        EditRoutineExerciseDialog fragment = new EditRoutineExerciseDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onSetDialogInteraction) {
            callback = (onSetDialogInteraction) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        callback = null;
    }

    @Override
    public void onDestroyView() {
        setsRecyclerView.setAdapter(null);
        setsRecyclerView = null;
        adapter = null;
        callback = null;
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);

            RoutinesViewModel routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);

            if (getArguments() != null) {
                idRoutineExercise = getArguments().getLong(INPUT_IDROUTINEEXERCISE);
                routineViewModel.getSetsForRoutineExercise(idRoutineExercise)
                        .subscribe(new SingleObserver<List<ExerciseSet>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<ExerciseSet> exerciseSets) {
                                setAdapterData(exerciseSets);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        }

    }

    /**
     * Given data, set the adapter data if the adapter was not created yet.
     **/
    private void setAdapterData(List<ExerciseSet> data) {
        if (setsRecyclerView != null && !dataWasInitialized) {
            adapter.setData(data);
            dataWasInitialized = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_routine_exercise_edit_dialog, container, false);

        setsRecyclerView = mView.findViewById(R.id.rv_routine_exercise_setsList);
        if (getContext() != null) {
            setsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            setsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
        setsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExerciseSetsAdapter();
        setsRecyclerView.setAdapter(adapter);
        setAdapterData();

        mView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
        mView.findViewById(R.id.btn_save).setOnClickListener(v ->
        {
            if (callback != null) {
                callback.onSave(adapter.getSets(), adapter.getDeletedSets());
                dismiss();
            }
        });

        return mView;
    }

    /**
     * If the adapter was not created before the data was obtained, then the data was stored in a temporary list.
     * So now that the adapter is created, add the temporary list to the adapter.
     **/
    private void setAdapterData() {
        if (setsData_temporary != null && !setsData_temporary.isEmpty()) {
            setAdapterData(setsData_temporary);
            setsData_temporary.clear();
        }
    }

    interface onSetDialogInteraction {
        void onSave(List<ExerciseSet> completeData, List<ExerciseSet> setsToDelete); // This will include both existing sets and new sets. If the set does not have an idExerciseSet, then it needs to be inserted into the database.
    }
}
