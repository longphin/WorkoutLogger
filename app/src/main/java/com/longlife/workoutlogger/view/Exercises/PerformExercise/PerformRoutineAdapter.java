package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    private IOnSetClick onSetClickListener;

    private static Integer performingHeaderIndex;
    private static Integer performingSetIndexWithinHeader;

    public PerformRoutineAdapter(Context context, IOnSetClick onSetClickListener) {
        super(context);

        this.onSetClickListener = onSetClickListener;
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

        // Change "start rest" timer button if needed.
        final int headerIndex = getHeaderIndex(pos);
        final int setIndexWithinHeader = getSetIndexWithinHeader(pos);
        if (performingHeaderIndex != null && performingSetIndexWithinHeader != null
                && headerIndex == performingHeaderIndex && setIndexWithinHeader == performingSetIndexWithinHeader) {
            performHolder.getStartRestView().setBackgroundResource(R.drawable.ic_pause_black_24dp);//.setImageResource(R.drawable.ic_pause_black_24dp);// [TODO] not working properly
        }

        performHolder.getStartRestView().setOnClickListener(view ->
        {
            final int clickedPos = holder.getAdapterPosition();
            final SessionExerciseSet clickedSet = getSetAtPosition(clickedPos);
            if (clickedSet != null) {
                performingHeaderIndex = getHeaderIndex(clickedPos);
                performingSetIndexWithinHeader = getSetIndexWithinHeader(clickedPos);

                onSetClickListener.startRestTimer(view, performingHeaderIndex, performingSetIndexWithinHeader, clickedSet.getRestMinutes(), clickedSet.getRestSeconds());
            }
        });

        // [TODO] When rest timer finished, need to set performingHeaderIndex and performingSetIndexWithinHeader to null

        /*performHolder.getView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos), PerformSetDialog.EditingType.WEIGHT);
        });*/
    }

    @Override
    public int getSetLayout() {
        return R.layout.item_perform_routine_set;
    }

    @Override
    public RecyclerView.ViewHolder createSetViewHolder(View v) {
        return new PerformRoutineViewHolder(v);
    }

    public interface IOnSetClick {
        void onSetClick(@Nullable RoutineExerciseSetPositions positionHelper, PerformSetDialog.EditingType initialFocus);

        void startRestTimer(View v, int headerIndex, int setIndex, int minutes, int seconds);
    }
}
