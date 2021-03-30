package com.prudhvireddy.spacex_leanix.screens.master_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prudhvireddy.spacex_leanix.LaunchPadListQuery
import com.prudhvireddy.spacex_leanix.databinding.ItemLaunchpadBinding

class LaunchPadListAdapter :
    ListAdapter<LaunchPadListQuery.Launchpad, LaunchPadListAdapter.LaunchPadViewHolder>(
        LaunchPadDiffUtil()
    ) {

    class LaunchPadViewHolder(private val binding: ItemLaunchpadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LaunchPadListQuery.Launchpad?) {
            item?.let {
                binding.launchPad = it
            }
        }

        companion object {
            fun from(parent: ViewGroup): LaunchPadViewHolder {

                return LaunchPadViewHolder(
                    ItemLaunchpadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchPadViewHolder {
        return LaunchPadViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LaunchPadViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class LaunchPadDiffUtil : DiffUtil.ItemCallback<LaunchPadListQuery.Launchpad>() {
    override fun areItemsTheSame(
        oldItem: LaunchPadListQuery.Launchpad,
        newItem: LaunchPadListQuery.Launchpad
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: LaunchPadListQuery.Launchpad,
        newItem: LaunchPadListQuery.Launchpad
    ) = oldItem == newItem

}
