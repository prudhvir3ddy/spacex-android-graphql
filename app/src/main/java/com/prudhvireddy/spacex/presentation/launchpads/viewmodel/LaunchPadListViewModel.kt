package com.prudhvireddy.spacex.presentation.launchpads.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.prudhvireddy.spacex.domain.LOAD_SIZE
import com.prudhvireddy.spacex.domain.LaunchPadListPagingSource
import com.prudhvireddy.spacex.domain.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchPadListViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    val flow = Pager(
        PagingConfig(
            pageSize = LOAD_SIZE,
            enablePlaceholders = false,
            initialLoadSize = LOAD_SIZE
        )
    ) {
        LaunchPadListPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}