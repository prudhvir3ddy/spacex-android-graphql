package com.prudhvireddy.spacex.presentation.launchpads.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.prudhvireddy.spacex.LaunchPadListQuery
import com.prudhvireddy.spacex.databinding.ItemLaunchpadBinding

class LaunchPadListAdapter(
    private val onItemClicked: (LaunchPadListQuery.Launchpad) -> Unit
) :
    PagingDataAdapter<LaunchPadListQuery.Launchpad, LaunchPadListAdapter.LaunchPadViewHolder>(
        LaunchPadDiffUtil()
    ) {

    class LaunchPadViewHolder(
        private val binding: ItemLaunchpadBinding,
        private val onItemClicked: (LaunchPadListQuery.Launchpad) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LaunchPadListQuery.Launchpad?) {
            item?.let { launchPad ->
                binding.launchPad = launchPad
                binding.root.setOnClickListener {
                    onItemClicked(launchPad)
                }
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onItemClicked: (LaunchPadListQuery.Launchpad) -> Unit
            ): LaunchPadViewHolder {

                return LaunchPadViewHolder(
                    ItemLaunchpadBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onItemClicked
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchPadViewHolder {
        return LaunchPadViewHolder.from(parent, onItemClicked)
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
