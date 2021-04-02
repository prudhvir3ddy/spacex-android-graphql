package com.prudhvireddy.spacex.domain

import android.content.SharedPreferences
import com.prudhvireddy.spacex.domain.SharedPrefs.PrefConstants.SORT

class SharedPrefs(
    private val
    sharedPreferences: SharedPreferences
) {

    object PrefConstants {
        const val SORT = "sort"
        const val PREFS = "prefs"
    }

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun getSort(): Boolean = sharedPreferences.getBoolean(SORT, false)

    fun setSorted(sort: Boolean) {
        editor.putBoolean(SORT, sort)
        editor.commit()
    }
}