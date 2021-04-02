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
import com.prudhvireddy.spacex.domain.LaunchPast
import com.prudhvireddy.spacex.domain.SharedPrefs
import com.prudhvireddy.spacex.presentation.launches.viewmodel.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class LaunchesFragment : Fragment(R.layout.fragment_launches) {

    private var _binding: FragmentLaunchesBinding? = null
    private val binding: FragmentLaunchesBinding get() = _binding!!

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private val onItemClicked = { position: Int, item: LaunchPast ->
        item.shouldExpand = item.shouldExpand.not()
        adapter.notifyItemChanged(position)
    }

    private val adapter: LaunchesListAdapter = LaunchesListAdapter(onItemClicked)

    private val args: LaunchesFragmentArgs by navArgs()

    private val viewModel: LaunchesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLaunchesBinding.bind(view)

        binding.rvLaunchList.adapter = adapter

        getPastLaunchesData()

        observeViewStates()
    }

    private fun observeViewStates() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
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
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.getLaunchesPast(args.siteId).collectLatest {
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
        _binding = null
    }

}