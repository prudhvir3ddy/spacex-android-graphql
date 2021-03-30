package com.prudhvireddy.spacex.presentation.master_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.domain.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterScreenViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    private val _launchpadList = MutableLiveData<List<LaunchPadListQuery.Launchpad?>?>()
    val launchPadList: LiveData<List<LaunchPadListQuery.Launchpad?>?> = _launchpadList

    fun getData() {
        viewModelScope.launch {
            _launchpadList.value = repository.getLaunchPadList()
        }
    }

}