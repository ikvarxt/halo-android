package me.ikvarxt.halo.ui.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.ikvarxt.halo.databinding.ItemPostCommentBinding
import me.ikvarxt.halo.entites.PostComment

class PostsCommentsAdapter :
    PagingDataAdapter<PostComment, PostsCommentsAdapter.ViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostCommentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ViewHolder(
        private val binding: ItemPostCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostComment) {
            binding.commentItem = item

            binding.executePendingBindings()
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<PostComment>() {
            override fun areContentsTheSame(oldItem: PostComment, newItem: PostComment): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: PostComment, newItem: PostComment): Boolean =
                oldItem.id == newItem.id
        }
    }
}