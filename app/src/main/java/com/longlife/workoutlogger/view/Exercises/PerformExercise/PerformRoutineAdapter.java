package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsAdapter;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import static com.longlife.workoutlogger.utils.Format.convertDoubleToStrWithoutZeroes;

public class PerformRoutineAdapter
        extends ExercisesWithSetsAdapter {
    private static final String TAG = PerformRoutineAdapter.class.getSimpleName();

    public PerformRoutineAdapter(Context context, IOnSetClick onSetClickListener) {
        super(context, onSetClickListener);
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
            performHolder.getWeightsTextView().setText(convertDoubleToStrWithoutZeroes(set.getWeights())); // [TODO] If the weight is an integer, do not show ending 0's (##). If there are any decimals, show up to 2 decimal places (##.00).
        else performHolder.getWeightsTextView().setText(R.string.zeroDouble);

        if (set.getReps() != null)
            performHolder.getRepsTextView().setText(String.valueOf(set.getReps()));
        else performHolder.getRepsTextView().setText(R.string.zeroInt);

        performHolder.getView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos));
        });
    }
}
