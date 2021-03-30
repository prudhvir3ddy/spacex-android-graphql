package com.prudhvireddy.spacex.presentation.master_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.domain.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterScreenViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    private val _launchpadList = MutableLiveData<ViewState>()
    val launchPadList: LiveData<ViewState> = _launchpadList

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _launchpadList.value = ViewState.Error(throwable.message.toString())
    }

    fun getData() {
        viewModelScope.launch(exceptionHandler) {
            _launchpadList.value = ViewState.Loading
            val response = repository.getLaunchPadList()
            response?.let {
                _launchpadList.value = ViewState.Success(it)
            } ?: throw Exception("no data found")
        }
    }

    sealed class ViewState {
        data class Success(val data: List<LaunchPadListQuery.Launchpad?>) : ViewState()
        object Loading : ViewState()
        data class Error(val error: String) : ViewState()
    }
}