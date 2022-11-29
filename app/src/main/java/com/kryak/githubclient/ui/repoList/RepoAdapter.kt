package com.kryak.githubclient.ui.repoList

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kryak.githubclient.databinding.RepositoryItemBinding
import com.kryak.githubclient.util.CustomClickListener
import com.kryak.model.RepoModel

class RepoAdapter(private val customClickListener: CustomClickListener) : ListAdapter<RepoModel, RecyclerView.ViewHolder>(RepoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RepoViewHolder(
            RepositoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            customClickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repository = getItem(position)
        (holder as RepoViewHolder).bind(repository)
    }

    class RepoViewHolder(
        private val binding: RepositoryItemBinding,
        private val customClickListener: CustomClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepoModel) {
            binding.apply {
                repositoryItemNameTv.text = item.name
                repositoryItemStarTv.text = item.star.toString()
                repositoryItemBrowserBt.setOnClickListener {
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(item.url)
                    )
                    itemView.context.startActivity(urlIntent)
                }
                repositoryItemDownloadBt.setOnClickListener {
                    customClickListener.onItemClickListener(it, item)
                }
            }
        }
    }
}

private class RepoDiffCallback : DiffUtil.ItemCallback<RepoModel>() {
    override fun areItemsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
        return oldItem == newItem
    }
}

/*
val url: String =
                        "https://api.github.com/repos/${item.owner}/${item.name}/zipball/master"
                    val manager = itemView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.setDescription(item.name)
                    request.setTitle(item.name)
                    request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                    )
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${item.name}.zip");
                    manager.enqueue(request)
 */
