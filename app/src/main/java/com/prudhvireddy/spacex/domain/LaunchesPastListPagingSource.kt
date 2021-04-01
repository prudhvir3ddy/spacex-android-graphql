package com.prudhvireddy.spacex.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prudhvireddy.spacex.LaunchesPastListQuery
import javax.inject.Inject

const val LOAD_SIZE_LAUNCHES = 10

class LaunchesPastListPagingSource @Inject constructor(
    private val siteId: String,
    private val repository: SpaceXRepository
): PagingSource<Int, LaunchPast>() {
    override fun getRefreshKey(state: PagingState<Int, LaunchPast> ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LaunchPast> {
        return try {
            val offset = (params.key ?: START_INDEX)
            val limit = LOAD_SIZE_LAUNCHES
            val response = repository.getLaunchPastList(siteId, offset, limit)
            val nextKey = if (response.isNullOrEmpty()) {
                null
            } else {
                offset + LOAD_SIZE_LAUNCHES
            }

            LoadResult.Page(response, null, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

data class LaunchPast(
    val launchesPast: LaunchesPastListQuery.LaunchesPast,
    var shouldExpand: Boolean = false
)