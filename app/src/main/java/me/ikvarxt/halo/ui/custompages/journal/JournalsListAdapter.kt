package me.ikvarxt.halo.ui.custompages.journal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.noties.markwon.Markwon
import me.ikvarxt.halo.databinding.ItemJournalBinding
import me.ikvarxt.halo.entites.HaloJournal

class JournalsListAdapter(
    private val listener: Listener,
    private val markwon: Markwon
) : ListAdapter<HaloJournal, JournalsListAdapter.ViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemJournalBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        val binding = holder.binding
        markwon.setMarkdown(binding.content, item.sourceContent)
        binding.content.setOnLongClickListener {
            listener.editJournal(item)
            true
        }
        binding.root.setOnLongClickListener {
            listener.editJournal(item)
            true
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<HaloJournal>() {
            override fun areContentsTheSame(oldItem: HaloJournal, newItem: HaloJournal): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: HaloJournal, newItem: HaloJournal): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    class ViewHolder(
        val binding: ItemJournalBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun editJournal(journal: HaloJournal)
    }
}