package me.ikvarxt.halo.ui.posts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.ItemMainPostCardBinding
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.ui.posts.article.ArticleFragmentArgs

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
            if (item.thumbnail.isNotBlank()) {
//                // TODO: add placeholder to this imageview
                binding.thumbnail.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .asBitmap()
                    .load(Uri.parse(item.thumbnail))
                    .error(R.mipmap.ic_launcher)
                    .transform(RoundedCorners(itemView.resources.getDimensionPixelOffset(R.dimen.postListItemThumbnailCornerRadius)))
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_dashboard_24)
                    .into(binding.thumbnail)
            }
            binding.root.setOnClickListener {
                itemView.findNavController().navigate(R.id.articleFragment, Bundle().apply {
                    putLong("postId", item.id)
                })
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