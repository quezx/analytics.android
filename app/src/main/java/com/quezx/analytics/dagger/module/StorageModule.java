package com.quezx.analytics.dagger.module;


import com.quezx.analytics.App;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.model.UserModel;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

	@Provides
	SharedPreferencesUtility provideSharedPreferencesUtility (App application) {
		return new SharedPreferencesUtility(application);
	}

	@Provides
	UserModel provideUserDetail (SharedPreferencesUtility sharedPreferencesUtility) {
		return sharedPreferencesUtility.getCurrentUser();
	}

}
