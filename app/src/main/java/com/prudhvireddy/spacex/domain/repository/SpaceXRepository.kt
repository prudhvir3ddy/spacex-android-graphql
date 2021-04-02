package com.prudhvireddy.spacex.domain.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.LaunchesPastListQuery
import com.prudhvireddy.spacex.data.LaunchPast
import com.prudhvireddy.spacex.type.LaunchFind
import javax.inject.Inject

class SpaceXRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun getLaunchPadList(offset: Int, limit: Int): List<LaunchPadListQuery.Launchpad> {
        val response =
            apolloClient.query(
                LaunchPadListQuery(
                    offset = Input.fromNullable(offset),
                    limit = Input.fromNullable(limit)
                )
            ).await()
        return if (!response.hasErrors() && response.data?.launchpads != null) {
            val launchpads = response.data?.launchpads!!
            launchpads.filterNotNull()
        } else {
            throw Exception(response.errors?.toString())
        }
    }

    suspend fun getLaunchPastList(
        siteId: String,
        offset: Int,
        limit: Int
    ): List<LaunchesPastListQuery.LaunchesPast> {
        val response = apolloClient.query(
            LaunchesPastListQuery(
                find = Input.fromNullable(
                    LaunchFind(
                        site_id = Input.fromNullable(siteId)
                    )
                ),
                limit = Input.fromNullable(limit),
                offset = Input.fromNullable(offset)
            )
        ).await()
        return if (!response.hasErrors() && response.data?.launchesPast != null) {
            val launches = response.data?.launchesPast!!
            launches.filterNotNull()
        } else {
            throw Exception(response.errors?.toString())
        }
    }

}