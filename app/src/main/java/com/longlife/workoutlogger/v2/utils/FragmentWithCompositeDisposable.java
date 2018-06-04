package com.longlife.workoutlogger.v2.utils;

import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FragmentWithCompositeDisposable extends Fragment {
    private CompositeDisposable composite = new CompositeDisposable();

    public void addDisposable(Disposable d) {
        composite.add(d);
    }

    public void clearDisposables() {
        composite.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearDisposables();
    }
}
