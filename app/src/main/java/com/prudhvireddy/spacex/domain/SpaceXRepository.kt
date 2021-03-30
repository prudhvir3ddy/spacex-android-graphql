package com.prudhvireddy.spacex.domain

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.prudhvireddy.spacex.LaunchPadListQuery
import javax.inject.Inject

class SpaceXRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun getLaunchPadList(offset: Int, limit: Int): List<LaunchPadListQuery.Launchpad?> {
        val response =
            apolloClient.query(
                LaunchPadListQuery(
                    offset = Input.fromNullable(offset),
                    limit = Input.fromNullable(limit)
                )
            ).await()
        return if (!response.hasErrors() && response.data?.launchpads != null) {
            response.data?.launchpads!!
        } else {
            throw Exception(response.errors?.toString())
        }
    }

}