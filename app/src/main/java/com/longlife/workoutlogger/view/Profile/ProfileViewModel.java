/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/9/18 11:05 AM.
 */

package com.longlife.workoutlogger.view.Profile;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.MuscleEntity;
import com.longlife.workoutlogger.model.MuscleGroupEntity;
import com.longlife.workoutlogger.model.Profile;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel
        extends ViewModel {
    private Repository repo;
    private Profile cachedProfile;

    public ProfileViewModel(@NonNull Repository repo) {
        this.repo = repo;
    }


    public Profile getCachedProfile() {
        return cachedProfile;
    }


    public void setCachedProfile(Profile cachedProfile) {
        this.cachedProfile = cachedProfile;
    }

    public Maybe<Profile> getProfile() {
        return repo.getProfile().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Profile> insertProfile(Profile profileToInsert) {
        return repo.insertProfile(profileToInsert).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void insertMuscles(List<MuscleEntity> muscles) {
        repo.insertMuscles(muscles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Long>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Long> longs) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void insertMuscleGroups(List<MuscleGroupEntity> groups) {
        repo.insertMuscleGroups(groups)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Long>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Long> longs) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
