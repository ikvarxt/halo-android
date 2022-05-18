package me.ikvarxt.halo.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.ItemMainPostCardBinding
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.ui.posts.post.PostFragment

class PostsListPagingAdapter(
    private val listener: Listener
) : PagingDataAdapter<PostItem, PostsListPagingAdapter.ViewHolder>(CALLBACK) {

    class ViewHolder(
        val binding: ItemMainPostCardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        val binding = holder.binding

        binding.post = item
        binding.root.setOnClickListener {
            it.findNavController().navigate(R.id.postFragment, Bundle().apply {
                putInt("postId", item.id)
                putInt("highlightId", PostFragment.NO_HIGHLIGHT_ID)
            })
        }
        binding.root.setOnLongClickListener {
            listener.deletePostPermanently(item)
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMainPostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    interface Listener {
        fun deletePostPermanently(item: PostItem)
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