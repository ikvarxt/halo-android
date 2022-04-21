package me.ikvarxt.halo.ui.posts

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.ItemMainPostCardBinding
import me.ikvarxt.halo.entites.PostItem

class PostsListPagingAdapter(
    private val listener: Listener
) : PagingDataAdapter<PostItem, PostsListPagingAdapter.ViewHolder>(
    PostsListAdapter.CALLBACK
) {

    inner class ViewHolder(
        private val binding: ItemMainPostCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItem) {
            binding.post = item
            if (item.thumbnail.isNotBlank()) {
                // TODO: add placeholder to this imageview
                binding.thumbnail.isVisible = true
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
                    putInt("postId", item.id)
                })
            }
            binding.root.setOnLongClickListener {
                listener.deletePostPermanently(item)
                true
            }
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMainPostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    interface Listener {
        fun deletePostPermanently(item: PostItem)
    }
}