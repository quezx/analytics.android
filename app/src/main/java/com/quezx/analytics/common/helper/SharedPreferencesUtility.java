package com.quezx.analytics.common.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quezx.analytics.common.constants.Random;
import com.quezx.analytics.model.AccessToken;
import com.quezx.analytics.model.UserModel;


import java.util.Calendar;

import io.branch.referral.Branch;


public class SharedPreferencesUtility {
	private static final String LOG_TAG = SharedPreferencesUtility.class.getSimpleName();
	private SharedPreferences sharedPreferences;
	private Context context;

	public SharedPreferencesUtility(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
	}

	public AccessToken getAccessToken () {
		AccessToken token = null;
		if (!sharedPreferences.getString(Random.SHARED_PREFERENCES_TOKEN_DATA, "").equals("")) {
			Gson gson = new GsonBuilder().create();
			token = gson.fromJson(sharedPreferences.getString(Random.SHARED_PREFERENCES_TOKEN_DATA, ""), AccessToken.class);
		}
		return token;
	}

	public void setAccessToken (AccessToken accessToken) {
		accessToken.start_time = Calendar.getInstance().getTimeInMillis();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Random.SHARED_PREFERENCES_TOKEN_DATA, new Gson().toJson(accessToken));
		editor.apply();
	}

	public void reset () {
		Branch.getInstance(context.getApplicationContext()).logout();
		sharedPreferences.edit().clear().apply();
	}

	public boolean isUserLoggedIn () {
		return getCurrentUser() != null;
	}

	public UserModel getCurrentUser () {
		UserModel user = null;
		if (!sharedPreferences.getString(Random.SHARED_PREFERENCES_CURRENT_USER, "").equals("")) {
			Gson gson = new GsonBuilder().create();
			user = gson.fromJson(sharedPreferences.getString(Random.SHARED_PREFERENCES_CURRENT_USER, ""), UserModel.class);
		}
		return user;
	}

	public void setCurrentUser (UserModel user) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Random.SHARED_PREFERENCES_CURRENT_USER, new Gson().toJson(user));
		editor.apply();
	}


	public void setCacheState(boolean state) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(Random.SHARED_PREFERENCES_CACHE_UPDATED, state);
		editor.apply();
	}

	public boolean isCacheUpdated() {
		return sharedPreferences.getBoolean(Random.SHARED_PREFERENCES_CACHE_UPDATED, false);
	}
}
