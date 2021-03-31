package com.prudhvireddy.spacex.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prudhvireddy.spacex.LaunchesPastListQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val LOAD_SIZE_LAUNCHES = 10

class LaunchesPastListPagingSource @Inject constructor(
    private val siteId: String,
    private val repository: SpaceXRepository
): PagingSource<Int, LaunchesPastListQuery.LaunchesPast>() {
    override fun getRefreshKey(state: PagingState<Int, LaunchesPastListQuery.LaunchesPast>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LaunchesPastListQuery.LaunchesPast> {
        return try {
            val offset = (params.key ?: START_INDEX)
            val limit = LOAD_SIZE_LAUNCHES
            val response = repository.getLaunchPastList(siteId, offset, limit)
            val nextKey = if (response.isNullOrEmpty()) {
                null
            } else {
                offset + LOAD_SIZE_LAUNCHES
            }

            val nonNullList = mutableListOf<LaunchesPastListQuery.LaunchesPast>()
            withContext(Dispatchers.Default) {
                response.forEach {
                    it?.let { launchpast ->
                        nonNullList.add(launchpast)
                    }
                }
            }

            LoadResult.Page(nonNullList, null, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}