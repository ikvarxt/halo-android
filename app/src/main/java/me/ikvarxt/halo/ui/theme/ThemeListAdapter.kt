package me.ikvarxt.halo.ui.theme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.ikvarxt.halo.databinding.ItemThemeBinding
import me.ikvarxt.halo.entites.HaloTheme

class ThemeListAdapter(
    private val listener: Listener
) : ListAdapter<HaloTheme, ThemeListAdapter.ViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemThemeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.theme = item

        holder.binding.root.setOnClickListener {
            if (!item.activated) {
                listener.selectActivate(theme = item)
            }
        }
    }

    class ViewHolder(val binding: ItemThemeBinding) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun selectActivate(theme: HaloTheme)
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<HaloTheme>() {
            override fun areItemsTheSame(oldItem: HaloTheme, newItem: HaloTheme): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HaloTheme, newItem: HaloTheme): Boolean {
                return oldItem == newItem
            }
        }
    }
}