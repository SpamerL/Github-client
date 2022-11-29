package com.kryak.githubclient.ui.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kryak.githubclient.databinding.DownloadItemBinding
import com.kryak.model.DownloadModel

class DownloadAdapter : ListAdapter<DownloadModel, RecyclerView.ViewHolder>(DownloadDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DownloadViewHolder(
            DownloadItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val download = getItem(position)
        (holder as DownloadViewHolder).bind(download)
    }

    class DownloadViewHolder(
        private val binding: DownloadItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DownloadModel) {
            binding.apply {
                downloadItemOwnerTv.text = item.userName
                downloadItemRepositoryTv.text = item.repositoryName
            }
        }
    }
}

private class DownloadDiffCallback : DiffUtil.ItemCallback<DownloadModel>() {
    override fun areItemsTheSame(oldItem: DownloadModel, newItem: DownloadModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DownloadModel, newItem: DownloadModel): Boolean {
        return oldItem == newItem
    }
}
