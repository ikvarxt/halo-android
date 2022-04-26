package me.ikvarxt.halo.ui.assets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.databinding.FragmentAssetsBinding
import me.ikvarxt.halo.databinding.ItemAttachmentBinding
import me.ikvarxt.halo.entites.Attachment

@AndroidEntryPoint
class AssetsFragment : Fragment() {

    private lateinit var binding: FragmentAssetsBinding
    private val viewModel by viewModels<AssetsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAssetsBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AssetsListAdapter()

        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.attachments.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

}

class AssetsListAdapter : PagingDataAdapter<Attachment, AssetsListAdapter.ViewHolder>(CALLBACK) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAttachmentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(
        private val itemBinding: ItemAttachmentBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Attachment) {
            itemBinding.attachment = item
            itemBinding.executePendingBindings()
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<Attachment>() {
            // TODO: 看一下这玩意儿究竟是怎么用的
            override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment) =
                oldItem == newItem
        }
    }
}