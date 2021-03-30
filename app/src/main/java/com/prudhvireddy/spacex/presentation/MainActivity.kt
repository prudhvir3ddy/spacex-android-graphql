package com.prudhvireddy.spacex.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.prudhvireddy.spacex.R
import com.prudhvireddy.spacex.presentation.master_screen.view.LaunchPadListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.commit {
            add<LaunchPadListFragment>(R.id.container)
        }
    }
}