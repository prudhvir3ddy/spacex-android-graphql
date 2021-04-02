package com.prudhvireddy.spacex.di

import android.content.Context
import android.content.SharedPreferences
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object StorageModule {

    @Singleton
    @Provides
    fun provideSharedPrefs(sharedPreferences: SharedPreferences): SpaceXSharedPrefs {
        return SpaceXSharedPrefs(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SpaceXSharedPrefs.PrefConstants.PREFS, Context.MODE_PRIVATE)
}