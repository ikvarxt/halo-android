package me.ikvarxt.halo.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.ikvarxt.halo.databinding.ItemMainPostCardBinding
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.Constants

class PostsListAdapter : ListAdapter<PostItem, PostsListAdapter.ViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMainPostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemMainPostCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItem) {
            binding.post = item
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.fullPath))
                val context = binding.root.context
                context.startActivity(intent)
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<PostItem>() {
            override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem) =
                oldItem == newItem
        }
    }


}