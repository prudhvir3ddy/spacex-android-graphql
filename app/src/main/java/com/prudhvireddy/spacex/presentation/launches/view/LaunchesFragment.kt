package com.prudhvireddy.spacex.presentation.launches.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.prudhvireddy.spacex.R
import com.prudhvireddy.spacex.databinding.FragmentLaunchesBinding
import com.prudhvireddy.spacex.presentation.launches.viewmodel.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LaunchesFragment : Fragment(R.layout.fragment_launches) {

    private var _binding: FragmentLaunchesBinding? = null
    private val binding: FragmentLaunchesBinding get() = _binding!!

    private val adapter = LaunchesListAdapter()

    private val args: LaunchesFragmentArgs by navArgs()

    private val viewModel: LaunchesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLaunchesBinding.bind(view)

        binding.rvLaunchList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.getLaunchesPast(args.siteId).collectLatest {
                adapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is LoadState.NotLoading -> binding.progressBar.visibility = View.GONE
                    is LoadState.Error -> Snackbar.make(
                        binding.rvLaunchList,
                        getString(R.string.something_went_wrong),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}