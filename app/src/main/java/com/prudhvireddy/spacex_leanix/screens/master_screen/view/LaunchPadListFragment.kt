package com.prudhvireddy.spacex_leanix.screens.master_screen.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prudhvireddy.spacex_leanix.R
import com.prudhvireddy.spacex_leanix.databinding.FragmentLaunchpadListBinding
import com.prudhvireddy.spacex_leanix.screens.master_screen.viewmodel.MasterScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchPadListFragment : Fragment(R.layout.fragment_launchpad_list) {

    private var _binding: FragmentLaunchpadListBinding? = null
    private val binding: FragmentLaunchpadListBinding
        get() = _binding!!

    private val viewModel: MasterScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        viewModel.getData()

        viewModel.launchPadList.observe(viewLifecycleOwner) {
            (binding.rvLaunchpadList.adapter as LaunchPadListAdapter).submitList(it)
        }

        binding.rvLaunchpadList.adapter = LaunchPadListAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}