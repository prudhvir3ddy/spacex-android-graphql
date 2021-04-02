package com.prudhvireddy.spacex.data

import com.prudhvireddy.spacex.LaunchesPastListQuery

data class LaunchPast(
    val launchesPast: LaunchesPastListQuery.LaunchesPast,
    var shouldExpand: Boolean = false
)