package com.prudhvireddy.spacex.presentation.launchpads.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.prudhvireddy.spacex.domain.LOAD_SIZE
import com.prudhvireddy.spacex.domain.LaunchPadListPagingSource
import com.prudhvireddy.spacex.domain.repository.SpaceXRepository
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchPadListViewModel @Inject constructor(
    private val repository: SpaceXRepository,
    private val sharedPrefs: SpaceXSharedPrefs
) : ViewModel() {

    val flow = Pager(
        PagingConfig(
            pageSize = LOAD_SIZE,
            enablePlaceholders = false,
            initialLoadSize = LOAD_SIZE
        )
    ) {
        LaunchPadListPagingSource(repository, sharedPrefs)
    }.flow
}