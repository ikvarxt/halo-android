package me.ikvarxt.halo.ui.posts

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.ikvarxt.halo.databinding.ItemMainPostCardBinding
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.entites.PostStatus
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

        if (item.status == PostStatus.DRAFT) {
            binding.postStatusBar.visibility = View.VISIBLE
        } else {
            binding.postStatusBar.visibility = View.INVISIBLE
        }

        val shouldShow = !TextUtils.isEmpty(item.thumbnail)
        val thumbnail = binding.thumbnail
        thumbnail.isVisible = shouldShow

        if (shouldShow) {
            Glide.with(thumbnail)
                .load(item.thumbnail)
                .into(thumbnail)
        } else {
            Glide.with(thumbnail).clear(thumbnail)
        }

        binding.root.setOnClickListener {
            val action = PostsListFragmentDirections.gotoPostsDetailsAction(
                item.id,
                PostFragment.NO_HIGHLIGHT_ID
            )
            it.findNavController().navigate(action)
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