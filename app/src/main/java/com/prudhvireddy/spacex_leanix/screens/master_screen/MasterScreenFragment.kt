package com.prudhvireddy.spacex_leanix.screens.master_screen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prudhvireddy.spacex_leanix.R
import com.prudhvireddy.spacex_leanix.databinding.FragmentMasterScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MasterScreenFragment : Fragment(R.layout.fragment_master_screen) {

    private var _binding: FragmentMasterScreenBinding? = null
    private val binding: FragmentMasterScreenBinding
        get() = _binding!!

    private val viewModel: MasterScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        viewModel.getData()

        viewModel.launchPadList.observe(viewLifecycleOwner) {
            Log.d("app-data", it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}