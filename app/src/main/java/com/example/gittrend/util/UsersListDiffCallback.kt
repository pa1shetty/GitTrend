package com.example.gittrend.util

import androidx.recyclerview.widget.DiffUtil
import com.example.gittrend.model.trending.TrendingListItem

class UsersListDiffCallback : DiffUtil.ItemCallback<TrendingListItem>() {

    override fun areItemsTheSame(oldItem: TrendingListItem, newItem: TrendingListItem): Boolean {
        return oldItem.rank == newItem.rank
    }

    override fun areContentsTheSame(oldItem: TrendingListItem, newItem: TrendingListItem): Boolean {
        return oldItem == newItem
    }
}