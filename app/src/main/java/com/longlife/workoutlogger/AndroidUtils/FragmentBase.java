package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

// Base fragment class that contains navigation and disposables logic.
public class FragmentBase
        extends Fragment {
    // Helps keep track of how many sub-fragments this fragment has.
    //protected FragmentNavigation fragmentNavigation;
    // Holds onto observables until cleared.
    private CompositeDisposable composite = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Attach to an activity/fragment with navigation manager.
        /*
        if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
        */
    }

    @Override
    public void onDestroy() {
        clearDisposables();
        super.onDestroy();
    }

    // Clear disposables.
    public void clearDisposables() {
        composite.clear();
    }

    // Add disposable.
    public void addDisposable(Disposable d) {
        composite.add(d);
    }

    protected void pushFragment(Fragment fragment) {
        if (getActivity() instanceof FragmentNavigation) {
            ((FragmentNavigation) getActivity()).pushFragment(fragment);
        }
    }

    // Interface to communicate from fragment to activity, so activity can add the fragment to the navigation manager.
    public interface FragmentNavigation {
        // When adding a sub-fragment to this fragment, notify the activity of this addition.
        void pushFragment(Fragment fragment);
    }
}

