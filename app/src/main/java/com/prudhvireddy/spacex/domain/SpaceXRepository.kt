package com.prudhvireddy.spacex.domain

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.prudhvireddy.spacex.LaunchPadListQuery
import javax.inject.Inject

class SpaceXRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun getLaunchPadList(): List<LaunchPadListQuery.Launchpad?>? {
        val response = apolloClient.query(LaunchPadListQuery()).await()
        return if (!response.hasErrors()) {
            response.data?.launchpads
        } else {
            throw Exception(response.errors?.toString())
        }
    }

}