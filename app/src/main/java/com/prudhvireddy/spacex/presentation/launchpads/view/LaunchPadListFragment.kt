package com.prudhvireddy.spacex.presentation.launchpads.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.R
import com.prudhvireddy.spacex.databinding.FragmentLaunchpadListBinding
import com.prudhvireddy.spacex.presentation.launchpads.viewmodel.LaunchPadListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LaunchPadListFragment : Fragment(R.layout.fragment_launchpad_list) {

    private var _binding: FragmentLaunchpadListBinding? = null
    private val binding: FragmentLaunchpadListBinding
        get() = _binding!!

    private val viewModel: LaunchPadListViewModel by viewModels()

    private val onItemClick = { launchPad: LaunchPadListQuery.Launchpad ->
        launchPad.id?.let {
            val action =
                LaunchPadListFragmentDirections.actionLaunchPadListFragmentToLaunchesFragment(
                    it
                )
            findNavController().navigate(action)
        }
        Unit
    }

    private val adapter: LaunchPadListAdapter = LaunchPadListAdapter(onItemClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)
        observeLaunchPadListDataFlow()

        binding.rvLaunchpadList.adapter = adapter

    }

    private fun observeLaunchPadListDataFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is LoadState.NotLoading -> binding.progressBar.visibility = View.GONE
                    is LoadState.Error -> Snackbar.make(
                        binding.rvLaunchpadList,
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