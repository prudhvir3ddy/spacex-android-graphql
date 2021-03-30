package com.prudhvireddy.spacex.presentation.master_screen.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.prudhvireddy.spacex.R
import com.prudhvireddy.spacex.databinding.FragmentLaunchpadListBinding
import com.prudhvireddy.spacex.presentation.master_screen.viewmodel.MasterScreenViewModel
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
        observeLaunchPadListData()
        binding.rvLaunchpadList.adapter = LaunchPadListAdapter()
    }

    private fun observeLaunchPadListData() {
        viewModel.launchPadList.observe(viewLifecycleOwner) {
            when (it) {
                MasterScreenViewModel.ViewState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MasterScreenViewModel.ViewState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.rvLaunchpadList,
                        it.error,
                        Snackbar.LENGTH_SHORT
                    ).show()

                }
                is MasterScreenViewModel.ViewState.Success -> {
                    (binding.rvLaunchpadList.adapter as LaunchPadListAdapter).submitList(it.data)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}