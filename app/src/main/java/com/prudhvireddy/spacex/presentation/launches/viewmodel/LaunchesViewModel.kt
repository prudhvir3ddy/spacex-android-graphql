package com.prudhvireddy.spacex.presentation.launches.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.prudhvireddy.spacex.domain.LOAD_SIZE_LAUNCHES
import com.prudhvireddy.spacex.domain.LaunchPast
import com.prudhvireddy.spacex.domain.LaunchesPastListPagingSource
import com.prudhvireddy.spacex.domain.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    fun getLaunchesPast(siteId: String): Flow<PagingData<LaunchPast>> {
        return Pager(
            PagingConfig(
                pageSize = LOAD_SIZE_LAUNCHES,
                enablePlaceholders = false,
                initialLoadSize = LOAD_SIZE_LAUNCHES
            )
        ) {
            LaunchesPastListPagingSource(siteId, repository)
        }.flow.cachedIn(viewModelScope)
    }

}