package com.example.gittrend.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gittrend.databinding.RepoItemBinding
import com.example.gittrend.model.trending.TrendingListItem
import com.example.gittrend.util.UsersListDiffCallback
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class TrendingRepoListAdapter @Inject constructor() :
    ListAdapter<TrendingListItem, TrendingRepoListAdapter.ViewHolder>(UsersListDiffCallback()) {
    var onItemClick: ((TrendingListItem) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        onItemClick?.let { holder.bind(item, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val binding: RepoItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TrendingListItem, onItemClick: ((TrendingListItem) -> Unit)) {
            itemView.setOnClickListener {
                onItemClick.invoke(item)
                binding.cvRepo.toggle()
            }

            binding.tvRepoName.text = item.repositoryName
            item.username?.let {
                binding.tvOwnerName.text = "$it / "
            }

            if (!item.description.isNullOrEmpty()) {
                binding.tvDescription.text = item.description
                binding.tvDescription.visibility = View.VISIBLE
            } else {
                binding.tvDescription.visibility = View.GONE
            }

            if (!item.language.isNullOrEmpty()) {
                binding.tvLanguage.text = item.language
                binding.tvLanguage.visibility = View.VISIBLE
                binding.ivLanguage.visibility = View.VISIBLE
                item.languageColor?.let { languageColor ->
                    binding.ivLanguage.setCardBackgroundColor(Color.parseColor(languageColor))
                }
            } else {
                binding.tvLanguage.visibility = View.GONE
                binding.ivLanguage.visibility = View.GONE
            }
            item.totalStars?.let {
                binding.tvStar.text = it.toString()
            }
            item.forks?.let {
                binding.tvFork.text = it.toString()
            }
            binding.cvRepo.isChecked = item.isChecked
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RepoItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}




