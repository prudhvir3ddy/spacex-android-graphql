package com.prudhvireddy.spacex.domain

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.LaunchesPastListQuery
import com.prudhvireddy.spacex.type.LaunchFind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            val nonNullList = mutableListOf<LaunchPadListQuery.Launchpad>()
            withContext(Dispatchers.Default) {
                launchpads.forEach {
                    it?.let { launchpad ->
                        nonNullList.add(launchpad)
                    }
                }
            }
            nonNullList
        } else {
            throw Exception(response.errors?.toString())
        }
    }

    suspend fun getLaunchPastList(
        siteId: String,
        offset: Int,
        limit: Int
    ): List<LaunchPast> {
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
            val nonNullList = mutableListOf<LaunchPast>()
            withContext(Dispatchers.Default) {
                launches.forEach {
                    it?.let { launchpad ->
                        nonNullList.add(LaunchPast(launchpad))
                    }
                }
            }
            nonNullList
        } else {
            throw Exception(response.errors?.toString())
        }
    }

}