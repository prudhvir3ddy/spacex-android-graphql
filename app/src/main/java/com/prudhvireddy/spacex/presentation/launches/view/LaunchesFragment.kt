package com.prudhvireddy.spacex.presentation.launches.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.prudhvireddy.spacex.R
import com.prudhvireddy.spacex.data.LaunchPast
import com.prudhvireddy.spacex.databinding.FragmentLaunchesBinding
import com.prudhvireddy.spacex.domain.storage.SpaceXSharedPrefs
import com.prudhvireddy.spacex.presentation.launches.viewmodel.LaunchesViewModel
import com.prudhvireddy.spacex.presentation.launches.viewmodel.LaunchesViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LaunchesFragment : Fragment(R.layout.fragment_launches) {

    private var _binding: FragmentLaunchesBinding? = null
    private val binding: FragmentLaunchesBinding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var launchesViewModelFactory: LaunchesViewModelFactory

    private val onItemClicked = { position: Int, item: LaunchPast ->
        item.shouldExpand = item.shouldExpand.not()
        adapter.notifyItemChanged(position)
    }

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener? =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            Log.d("boom", key)
            if (key == SpaceXSharedPrefs.PrefConstants.SORT) {
                restartFetch()
            }
        }

    private fun restartFetch() {
        job?.cancel()
        getPastLaunchesData()
    }

    private val adapter: LaunchesListAdapter = LaunchesListAdapter(onItemClicked)

    private val args: LaunchesFragmentArgs by navArgs()

    private val viewModel: LaunchesViewModel by viewModels {
        LaunchesViewModel.provideFactory(launchesViewModelFactory, args.siteId)
    }

    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLaunchesBinding.bind(view)
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        binding.rvLaunchList.adapter = adapter

        getPastLaunchesData()

        observeViewStates()
    }

    private fun observeViewStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    binding.rvLaunchList.scrollToPosition(0)
                }

            adapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is LoadState.NotLoading -> binding.progressBar.visibility = View.GONE
                    is LoadState.Error -> {
                        Snackbar.make(
                            binding.rvLaunchList,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.GONE
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

    private fun getPastLaunchesData() {
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLaunchesPast().collect {
                adapter.submitData(it)
            }
        }
    }

    private fun showNoContentView() {
        Snackbar.make(
            binding.rvLaunchList,
            getString(R.string.content_not_found),
            Snackbar.LENGTH_SHORT
        ).show()
        binding.rvLaunchList.visibility = View.GONE
        binding.ivEmpty.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        _binding = null
    }

}