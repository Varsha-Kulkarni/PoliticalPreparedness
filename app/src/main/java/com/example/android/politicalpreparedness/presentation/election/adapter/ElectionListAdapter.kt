package com.example.android.politicalpreparedness.presentation.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.domain.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    class ElectionViewHolder(val viewDataBinding: ItemElectionBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_election
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val binding: ItemElectionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), ElectionViewHolder.LAYOUT, parent, false)
        return ElectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val election = getItem(position)
            it.election = election
            it.clickListener = clickListener

            //For accessibility
            holder.itemView.contentDescription = "Election $position ${election.name} , on ${election.electionDay} at ${election.division}"
        }
    }

    class ElectionListener(val block: (Election) -> Unit) {
        fun onClick(election: Election) = block(election)
    }

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Election, newItem: Election) = oldItem == newItem

    }
}