package me.ikvarxt.halo.ui.posts.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.ikvarxt.halo.databinding.ItemPostCategoryBinding
import me.ikvarxt.halo.entites.PostCategory

class CategoriesListAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private val _list = mutableListOf<PostCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _list[position]
        val binding = holder.binding

        binding.name.text = item.name
        binding.root.setOnLongClickListener {
            listener.editCategory(item)
            true
        }
    }

    override fun getItemCount(): Int = _list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<PostCategory>) {
        if (_list != data) {
            _list.clear()
            _list.addAll(data)
            notifyDataSetChanged()
        }
    }

    class ViewHolder(
        val binding: ItemPostCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun editCategory(category: PostCategory)

    }
}