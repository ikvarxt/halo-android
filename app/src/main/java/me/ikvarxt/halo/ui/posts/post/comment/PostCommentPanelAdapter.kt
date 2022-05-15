package me.ikvarxt.halo.ui.posts.post.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import io.noties.markwon.Markwon
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.ItemPostCommentPanelChildBinding
import me.ikvarxt.halo.databinding.ItemPostCommentPanelListBinding
import me.ikvarxt.halo.entites.PostComment

class PostCommentPanelAdapter(
    private val markwon: Markwon
) :
    ListAdapter<PostComment, PostCommentPanelAdapter.ViewHolder>(CALLBACK) {

    /**
     * store the highlight position of comment item in the adapter
     */
    private var highlightPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ITEM_PARENT -> {
                val binding = ItemPostCommentPanelListBinding.inflate(inflater, parent, false)
                ViewHolder.ParentViewHolder(binding)
            }
            ITEM_CHILD -> {
                val binding = ItemPostCommentPanelChildBinding.inflate(inflater, parent, false)
                ViewHolder.ChildViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Not found this viewType: $viewType")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        if (item.isHighlight) {
            val binding = holder.b
            highlightPosition = holder.bindingAdapterPosition
            val context = binding.root.context

            val color =
                context.resources.getColor(R.color.purple_500, context.theme)
            binding.root.setBackgroundColor(color)
        }

        when (getItemViewType(position)) {
            ITEM_PARENT -> {
                val binding = (holder as ViewHolder.ParentViewHolder).binding
                markwon.setMarkdown(binding.commentContent, item.content)
            }
            ITEM_CHILD -> {
                val binding = (holder as ViewHolder.ChildViewHolder).binding
                markwon.setMarkdown(binding.commentContent, item.content)
            }
        }
    }

    sealed class ViewHolder(val b: ViewBinding) : RecyclerView.ViewHolder(b.root) {
        class ParentViewHolder(
            val binding: ItemPostCommentPanelListBinding
        ) : ViewHolder(binding)

        class ChildViewHolder(
            val binding: ItemPostCommentPanelChildBinding
        ) : ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return -1

        return if (item.parentId != 0) ITEM_CHILD
        else ITEM_PARENT
    }

    fun disableHighlight() = highlightPosition?.let {
        getItem(it).isHighlight = false
        notifyItemChanged(it)
    }

    companion object {

        const val ITEM_PARENT = 0
        const val ITEM_CHILD = 1

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