/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {
    public final static String decimalCharacter = "."; // [TODO] Change this to use the user's locale, such as "," instead for decimals.

    //private Locale locale = Locale.US;
    @PrimaryKey
    private Long idProfile;
    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getIdProfile() {
        return idProfile;
    }
	
	/*public Locale getLocale()
	
	{
		return locale;
	}*/


    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	/*public void setLocale(Locale l)
	{
		locale = l;
	}*/
}

