package com.prudhvireddy.spacex.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prudhvireddy.spacex.LaunchesPastListQuery
import com.prudhvireddy.spacex.domain.repository.SpaceXRepository
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val LOAD_SIZE_LAUNCHES = 10

class LaunchesPastListPagingSource @Inject constructor(
    private val siteId: String,
    private val repository: SpaceXRepository,
    private val sharedPrefs: SpaceXSharedPrefs
) : PagingSource<Int, LaunchesPastListQuery.LaunchesPast>() {
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
            var response = repository.getLaunchPastList(siteId, offset, limit)
            val nextKey = if (response.isNullOrEmpty()) {
                null
            } else {
                offset + LOAD_SIZE_LAUNCHES
            }
            if (sharedPrefs.getSort()) {
                response = withContext(Dispatchers.Default) {
                    response.sortedBy {
                        it.mission_name
                    }
                }
            }
            LoadResult.Page(response, null, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}