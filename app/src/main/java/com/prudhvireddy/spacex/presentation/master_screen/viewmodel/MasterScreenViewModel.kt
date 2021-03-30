package com.prudhvireddy.spacex.presentation.master_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.domain.LaunchPadListPagingSource
import com.prudhvireddy.spacex.domain.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MasterScreenViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    val flow = Pager(
        PagingConfig(
            pageSize = 5,
            enablePlaceholders = false,
            initialLoadSize = 5
        )
    ) {
        LaunchPadListPagingSource(repository)
    }.flow.cachedIn(viewModelScope)


    sealed class ViewState {
        data class Success(val data: List<LaunchPadListQuery.Launchpad?>) : ViewState()
        object Loading : ViewState()
        data class Error(val error: String) : ViewState()
    }
}