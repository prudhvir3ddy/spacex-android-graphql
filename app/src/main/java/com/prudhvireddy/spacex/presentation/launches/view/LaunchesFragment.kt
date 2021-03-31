package com.prudhvireddy.spacex.presentation.launches.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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

    companion object {
        const val SITE_ID = "site_id"
    }

    private val viewModel: LaunchesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLaunchesBinding.bind(view)

        binding.rvLaunchList.adapter = adapter
        arguments?.getString(SITE_ID)?.let {
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.getLaunchesPast(it).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}