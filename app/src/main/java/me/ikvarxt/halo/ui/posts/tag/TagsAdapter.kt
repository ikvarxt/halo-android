package me.ikvarxt.halo.ui.posts.tag

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.ikvarxt.halo.databinding.ItemPostTagBinding
import me.ikvarxt.halo.entites.PostTag

class TagsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    private val _list = mutableListOf<PostTag>()

    class ViewHolder(val binding: ItemPostTagBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostTagBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _list[position]
        val binding = holder.binding
        binding.tag = item

        val color = Color.parseColor(item.color)
        binding.tagColor.setBackgroundColor(color)

        binding.root.setOnClickListener { listener.showTag(item) }
    }

    override fun getItemCount(): Int = _list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: List<PostTag>) {
        if (list != _list) {
            _list.clear()
            _list.addAll(list)
            notifyDataSetChanged()
        }
    }

    interface Listener {
        fun showTag(tag: PostTag)
    }
}