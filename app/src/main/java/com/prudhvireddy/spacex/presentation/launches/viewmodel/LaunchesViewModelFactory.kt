package com.prudhvireddy.spacex.presentation.launches.viewmodel

import dagger.assisted.AssistedFactory


@AssistedFactory
interface LaunchesViewModelFactory {
    fun create(siteId: String): LaunchesViewModel
}