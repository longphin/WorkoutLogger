/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsAdapter;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.longlife.workoutlogger.utils.Format.convertDoubleToStrWithoutZeroes;

public class PerformRoutineAdapter
        extends ExercisesWithSetsAdapter {
    private static final String TAG = PerformRoutineAdapter.class.getSimpleName();

    private IOnSetClick onSetClickListener;

    PerformRoutineAdapter(Context context, IOnSetClick onSetClickListener) {
        super(context);

        this.onSetClickListener = onSetClickListener;
    }

    @Override
    public int getSetLayout() {
        return R.layout.item_perform_routine_set;
    }

    @Override
    public RecyclerView.ViewHolder createSetViewHolder(View v) {
        return new PerformRoutineViewHolder(v);
    }

    @Override
    public void bindSetViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof PerformRoutineViewHolder)) {
            Log.e(TAG, "ViewHolder is not of the proper instance.");
            return;
        }

        PerformRoutineViewHolder performHolder = (PerformRoutineViewHolder) holder;

        final int pos = holder.getAdapterPosition();
        final SessionExerciseSet set = getSetAtPosition(pos);
        performHolder.getRestTextView().setText(context.getString(R.string.set_header, set.getRestMinutes(), set.getRestSeconds()));

        if (set.getWeights() != null)
            performHolder.getWeightsTextView().setText(convertDoubleToStrWithoutZeroes(set.getWeights()));
        else performHolder.getWeightsTextView().setText(R.string.zeroDouble);

        if (set.getReps() != null)
            performHolder.getRepsTextView().setText(String.valueOf(set.getReps()));
        else performHolder.getRepsTextView().setText(R.string.zeroInt);

        // Set checkbox.
        if (set.isPerformed()) {
            performHolder.getStartRestView().setImageResource(R.drawable.ic_check_black_24dp);//R.drawable.ic_pause_black_24dp);//.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        } else {
            performHolder.getStartRestView().setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);//R.drawable.ic_pause_black_24dp);//.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        }

        performHolder.getWeightsTextView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos), PerformSetDialog.EditingType.WEIGHT);
        });

        performHolder.getRepsTextView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos), PerformSetDialog.EditingType.REP);
        });

        performHolder.getRestTextView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos), PerformSetDialog.EditingType.REST);
        });

        performHolder.getStartRestView().setOnClickListener(view ->
        {
            final int clickedPos = holder.getAdapterPosition();
            final SessionExerciseSet clickedSet = getSetAtPosition(clickedPos);
            if (clickedSet != null) {
                // Visually change the checkbox.
                clickedSet.setPerformed(!clickedSet.isPerformed());
                if (clickedSet.isPerformed()) {
                    // Set checkbox as TRUE.
                    performHolder.getStartRestView().setImageResource(R.drawable.ic_check_black_24dp);//R.drawable.ic_pause_black_24dp);//.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    // Start the rest timer.
                    final int performingHeaderIndex = getHeaderIndex(clickedPos);
                    final int performingSetIndexWithinHeader = getSetIndexWithinHeader(clickedPos);
                    onSetClickListener.startRestTimer(view, performingHeaderIndex, performingSetIndexWithinHeader, clickedSet.getRestMinutes(), clickedSet.getRestSeconds());
                } else {
                    // Set checkbox as FALSE.
                    performHolder.getStartRestView().setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);//R.drawable.ic_pause_black_24dp);//.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }

            }
        });
    }

    public interface IOnSetClick {
        void onSetClick(@Nullable RoutineExerciseSetPositions positionHelper, PerformSetDialog.EditingType initialFocus);

        void startRestTimer(View v, int headerIndex, int setIndex, int minutes, int seconds);
    }
}
