package com.longlife.workoutlogger.v2.utils;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class ViewModelWithCompositeDisposable extends ViewModel {
    public CompositeDisposable composite = new CompositeDisposable();

    public void addDisposable(Disposable d) {
        composite.add(d);
    }

    public void clearComposite() {
        composite.clear();
    }
}
