/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/28/18 6:53 PM.
 */

package com.longlife.workoutlogger.view.Routines.CreateRoutine;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsAdapter;
import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsViewHolder;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets.RoutineCreateSetViewHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineCreateAdapter
        extends ExercisesWithSetsAdapter {
    private static final String TAG = RoutineCreateAdapter.class.getSimpleName();

    private IOnSetClick onSetClickListener;

    RoutineCreateAdapter(Context context, IOnSetClick onSetClickListener) {
        super(context);

        this.onSetClickListener = onSetClickListener;
    }

    @Override
    public int getSetLayout() {
        return R.layout.item_routine_create_exercise_set;
    }

    @Override
    public RecyclerView.ViewHolder createSetViewHolder(View v) {
        return new RoutineCreateSetViewHolder(v);
    }

    @Override
    public void bindSetViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof ExercisesWithSetsViewHolder)) {
            Log.e(TAG, "View holder is not of the proper instance.");
            return;
        }

        ExercisesWithSetsViewHolder routineHolder = (ExercisesWithSetsViewHolder) holder;

        final int pos = holder.getAdapterPosition();
        final SessionExerciseSet set = getSetAtPosition(pos);
        routineHolder.getRestTextView().setText(context.getString(R.string.set_header, set.getRestMinutes(), set.getRestSeconds()));

        routineHolder.getView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos));
        });
    }

    public interface IOnSetClick {
        void onSetClick(@Nullable RoutineExerciseSetPositions positionHelper);
    }
}
