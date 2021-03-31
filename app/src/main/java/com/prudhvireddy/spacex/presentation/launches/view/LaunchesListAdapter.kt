package com.prudhvireddy.spacex.presentation.launches.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.prudhvireddy.spacex.databinding.ItemLaunchBinding
import com.prudhvireddy.spacex.domain.LaunchPast

class LaunchesListAdapter :
    PagingDataAdapter<LaunchPast, LaunchesListAdapter.LaunchesViewHolder>(
        LaunchesDiffUtil()
    ) {

    class LaunchesViewHolder(private val binding: ItemLaunchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LaunchPast?) {
            item?.let {
                binding.launch = it.launchesPast
                binding.shouldShow = it.shouldExpand
                binding.rootLaunchCard.setOnClickListener {
                    item.shouldExpand = item.shouldExpand.not()
                    bindingAdapter?.notifyItemChanged(position)
                }
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

class LaunchesDiffUtil : DiffUtil.ItemCallback<LaunchPast>() {
    override fun areItemsTheSame(
        oldItem: LaunchPast,
        newItem: LaunchPast
    ): Boolean = oldItem.launchesPast.id == newItem.launchesPast.id

    override fun areContentsTheSame(
        oldItem: LaunchPast,
        newItem: LaunchPast
    ) = oldItem.launchesPast == newItem.launchesPast

}

