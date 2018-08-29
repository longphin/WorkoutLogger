package com.longlife.workoutlogger.view.Profile;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.Profile;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel
	extends ViewModel
{
	private Repository repo;
	private Profile cachedProfile;
	
	public ProfileViewModel(@NonNull Repository repo)
	{
		this.repo = repo;
	}
	
	// Getters
	public Profile getCachedProfile()
	{
		return cachedProfile;
	}
	
	public Maybe<Profile> getProfile()
	{
		return repo.getProfile().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
	}
	
	// Setters
	public void setCachedProfile(Profile cachedProfile)
	{
		this.cachedProfile = cachedProfile;
	}
	
	public Single<Profile> insertProfile(Profile profileToInsert)
	{
		return repo.insertProfile(profileToInsert).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
	}
	
}
