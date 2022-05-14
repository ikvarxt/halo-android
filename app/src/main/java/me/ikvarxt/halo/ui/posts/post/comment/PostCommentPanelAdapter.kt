package me.ikvarxt.halo.ui.posts.post.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.ikvarxt.halo.databinding.ItemPostCommentPanelListBinding
import me.ikvarxt.halo.entites.PostComment

class PostCommentPanelAdapter :
    ListAdapter<PostComment, PostCommentPanelAdapter.ViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostCommentPanelListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.binding.commentContent.text = item.content
        }
    }

    class ViewHolder(val binding: ItemPostCommentPanelListBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {

        private val CALLBACK = object : DiffUtil.ItemCallback<PostComment>() {
            override fun areItemsTheSame(oldItem: PostComment, newItem: PostComment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PostComment, newItem: PostComment): Boolean {
                return oldItem == newItem
            }

        }
    }


}