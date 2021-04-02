package com.prudhvireddy.spacex.domain

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.domain.repository.SpaceXRepository
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val START_INDEX = 0
const val LOAD_SIZE = 5

class LaunchPadListPagingSource @Inject constructor(
    private val repository: SpaceXRepository,
    private val sharedPrefs: SpaceXSharedPrefs
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
            var response = repository.getLaunchPadList(offset, limit)
            Log.d("boom", "$offset $limit")
            val nextKey = if (response.isNullOrEmpty()) {
                null
            } else {
                offset + LOAD_SIZE
            }

            if (sharedPrefs.getSort()) {
                response = withContext(Dispatchers.Default) {
                    response.sortedBy {
                        it.name
                    }
                }
            }
            LoadResult.Page(response, null, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

}



