package com.prudhvireddy.spacex.presentation.launchpads.view

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.R
import com.prudhvireddy.spacex.databinding.FragmentLaunchpadListBinding
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import com.prudhvireddy.spacex.presentation.launches.view.LaunchesFragmentDirections
import com.prudhvireddy.spacex.presentation.launchpads.viewmodel.LaunchPadListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LaunchPadListFragment : Fragment(R.layout.fragment_launchpad_list) {

    private var _binding: FragmentLaunchpadListBinding? = null
    private val binding: FragmentLaunchpadListBinding
        get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: LaunchPadListViewModel by viewModels()

    private var job: Job? = null

    lateinit var navController: NavController
    private val onItemClick = { launchPad: LaunchPadListQuery.Launchpad ->
        val orientation = resources.configuration.orientation
        launchPad.id?.let {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val action = LaunchesFragmentDirections.actionLaunchesFragment2Self(it)
                navController.navigate(action)
            } else {
                val action =
                    LaunchPadListFragmentDirections.actionLaunchPadListFragmentToLaunchesFragment(it)
                findNavController().navigate(action)
            }
        }
        Unit
    }

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == SpaceXSharedPrefs.PrefConstants.SORT) {
                restartFetch()
            }
        }

    private val adapter: LaunchPadListAdapter = LaunchPadListAdapter(onItemClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        _binding = DataBindingUtil.bind(view)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val navHostFragment =
                childFragmentManager.findFragmentById(binding.container!!.id) as NavHostFragment?

            navController = navHostFragment!!.navController

        }

        binding.rvLaunchpadList.adapter = adapter

        observeLaunchPadListDataFlow()
        observeViewState()
    }

    private fun restartFetch() {
        job?.cancel()
        observeLaunchPadListDataFlow()
    }

    private fun observeViewState() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvLaunchpadList.scrollToPosition(0) }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showLoadingView()
                    }
                    is LoadState.NotLoading -> {
                        showNewData()
                    }
                    is LoadState.Error -> {
                        Snackbar.make(
                            binding.rvLaunchpadList,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        showNoContentView()
                    }
                }
                if (loadState.append.endOfPaginationReached) {
                    if (adapter.itemCount < 1) {
                        showNoContentView()
                    }
                }
            }
        }
    }

    private fun showNewData() {
        binding.ivEmpty.visibility = View.GONE
        binding.rvLaunchpadList.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoadingView() {
        binding.rvLaunchpadList.visibility = View.GONE
        binding.ivEmpty.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun observeLaunchPadListDataFlow() {
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun showNoContentView() {
        Snackbar.make(
            binding.rvLaunchpadList,
            getString(R.string.content_not_found),
            Snackbar.LENGTH_SHORT
        ).show()
        binding.rvLaunchpadList.visibility = View.GONE
        binding.ivEmpty.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        _binding = null
    }
}