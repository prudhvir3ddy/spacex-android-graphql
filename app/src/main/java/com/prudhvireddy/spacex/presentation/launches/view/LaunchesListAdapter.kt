package com.prudhvireddy.spacex.presentation.launches.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.prudhvireddy.spacex.LaunchesPastListQuery
import com.prudhvireddy.spacex.databinding.ItemLaunchBinding

class LaunchesListAdapter :
    PagingDataAdapter<LaunchesPastListQuery.LaunchesPast, LaunchesListAdapter.LaunchesViewHolder>(
        LaunchesDiffUtil()
    ) {

    class LaunchesViewHolder(private val binding: ItemLaunchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LaunchesPastListQuery.LaunchesPast?) {
            item?.let {
                binding.launch = it
                binding.rootLaunchCard.setOnClickListener {
                    toggleVisibility(binding.detailContainer)
                }
            }
        }

        private fun toggleVisibility(detailContainer: ConstraintLayout) {
            if (detailContainer.isVisible) {
                detailContainer.visibility = View.GONE
            } else {
                detailContainer.visibility = View.VISIBLE
            }
        }

        companion object {
            fun from(parent: ViewGroup): LaunchesViewHolder {

                return LaunchesViewHolder(
                    ItemLaunchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchesViewHolder {
        return LaunchesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LaunchesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class LaunchesDiffUtil : DiffUtil.ItemCallback<LaunchesPastListQuery.LaunchesPast>() {
    override fun areItemsTheSame(
        oldItem: LaunchesPastListQuery.LaunchesPast,
        newItem: LaunchesPastListQuery.LaunchesPast
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: LaunchesPastListQuery.LaunchesPast,
        newItem: LaunchesPastListQuery.LaunchesPast
    ) = oldItem == newItem

}
