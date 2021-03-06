package com.prudhvireddy.spacex.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.prudhvireddy.spacex.R
import com.prudhvireddy.spacex.databinding.ActivityMainBinding
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var spaceXSharedPrefs: SpaceXSharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.container.id) as NavHostFragment?

        val navController = navHostFragment!!.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.menu.findItem(R.id.sort).setOnMenuItemClickListener {
            spaceXSharedPrefs.setSorted(!spaceXSharedPrefs.getSort())
            true
        }
    }

}