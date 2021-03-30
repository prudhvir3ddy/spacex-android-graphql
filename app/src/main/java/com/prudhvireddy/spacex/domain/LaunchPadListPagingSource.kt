package com.prudhvireddy.spacex.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prudhvireddy.spacex.LaunchPadListQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val START_INDEX = 0
const val LOAD_SIZE = 5

class LaunchPadListPagingSource @Inject constructor(
    private val repository: SpaceXRepository
) : PagingSource<Int, LaunchPadListQuery.Launchpad>() {
    override fun getRefreshKey(state: PagingState<Int, LaunchPadListQuery.Launchpad>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LaunchPadListQuery.Launchpad> {
        return try {
            val offset = (params.key ?: START_INDEX)
            val limit = LOAD_SIZE
            val response = repository.getLaunchPadList(offset, limit)
            val nextKey = if (response.isNullOrEmpty()) {
                null
            } else {
                offset + LOAD_SIZE
            }

            val nonNullList = mutableListOf<LaunchPadListQuery.Launchpad>()
            withContext(Dispatchers.Default) {
                response.forEach {
                    it?.let { launchpad ->
                        nonNullList.add(launchpad)
                    }
                }
            }

            LoadResult.Page(nonNullList, null, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

}


