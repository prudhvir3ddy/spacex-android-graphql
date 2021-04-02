package com.prudhvireddy.spacex.presentation.launches.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.prudhvireddy.spacex.data.LaunchPast
import com.prudhvireddy.spacex.domain.LOAD_SIZE_LAUNCHES
import com.prudhvireddy.spacex.domain.LaunchesPastListPagingSource
import com.prudhvireddy.spacex.domain.repository.SpaceXRepository
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LaunchesViewModel @AssistedInject constructor(
    private val repository: SpaceXRepository,
    private val sharedPrefs: SpaceXSharedPrefs,
    @Assisted private val siteId: String
) : ViewModel() {

    private val pager = Pager(
        PagingConfig(
            pageSize = LOAD_SIZE_LAUNCHES,
            enablePlaceholders = false,
            initialLoadSize = LOAD_SIZE_LAUNCHES
        )
    ) {
        LaunchesPastListPagingSource(siteId, repository, sharedPrefs)
    }

    fun getLaunchesPast(): Flow<PagingData<LaunchPast>> {
        return pager.flow.map { pagingData ->
            pagingData.map {
                LaunchPast(it)
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: LaunchesViewModelFactory,
            siteId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(siteId) as T
            }

        }
    }
}
