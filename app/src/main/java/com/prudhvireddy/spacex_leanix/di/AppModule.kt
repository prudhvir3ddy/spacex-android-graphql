package com.prudhvireddy.spacex_leanix.di

import com.apollographql.apollo.ApolloClient
import com.prudhvireddy.spacex_leanix.repository.SpaceXRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSpaceXRepo(apolloClient: ApolloClient) = SpaceXRepository(apolloClient)
}