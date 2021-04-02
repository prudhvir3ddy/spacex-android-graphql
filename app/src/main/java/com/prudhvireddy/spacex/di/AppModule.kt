package com.prudhvireddy.spacex.di

import android.content.Context
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.prudhvireddy.spacex.domain.SharedPrefs
import com.prudhvireddy.spacex.domain.SpaceXRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPrefs(sharedPreferences: SharedPreferences): SharedPrefs {
        return SharedPrefs(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SharedPrefs.PrefConstants.PREFS, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSpaceXRepo(apolloClient: ApolloClient) = SpaceXRepository(apolloClient)
}