package com.prudhvireddy.spacex.di

import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("base-url")
    fun provideSpaceXUrl(): String = "https://api.spacex.land/graphql/"

    @Provides
    @Singleton
    fun provideApolloClient(
        @Named("base-url") baseUrl: String
    ): ApolloClient = ApolloClient.builder()
        .serverUrl(baseUrl)
        .build()


}
