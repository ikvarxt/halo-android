package me.ikvarxt.halo.ui.assets

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.databinding.DialogUploadImageBinding
import me.ikvarxt.halo.databinding.FragmentAssetsBinding
import me.ikvarxt.halo.databinding.ItemAttachmentBinding
import me.ikvarxt.halo.entites.Attachment
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.ui.MainActivity

@AndroidEntryPoint
class AssetsFragment : Fragment(), AssetsListAdapter.Listener, MainActivity.RefreshListener {

    private lateinit var binding: FragmentAssetsBinding
    private val viewModel by viewModels<AssetsViewModel>()
    private lateinit var adapter: AssetsListAdapter

    private var contentCallback: ((Uri) -> Unit)? = null
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) contentCallback?.invoke(it)
        contentCallback = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAssetsBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AssetsListAdapter(this)

        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.attachments.collectLatest {
                    adapter.submitData(it)
                }
            }
            launch {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    binding.swipeRefreshLayout.isRefreshing =
                        loadStates.mediator?.refresh is LoadState.Loading
                }
            }
            launch {
                viewModel.refreshState.collectLatest {
                    if (it) adapter.refresh()
                }
            }
        }


        binding.fab.setOnClickListener {
            getContent("image/*") { uri ->
                val context = requireContext()
                val inflater = LayoutInflater.from(context)
                val dialogViewBinding =
                    DialogUploadImageBinding.inflate(inflater, null, false).apply {
                        fileUri = uri
                    }
                val positiveAction = DialogInterface.OnClickListener { _, _ ->
                    val text = dialogViewBinding.imageNameEdit.text
                    viewModel.publish(uri, text.toString())
                }
                MaterialAlertDialogBuilder(context)
                    .setView(dialogViewBinding.root)
                    .setPositiveButton("Upload", positiveAction)
                    .show()
            }
        }
    }

    private fun getContent(type: String, callback: (Uri) -> Unit) {
        contentCallback = callback
        getContent.launch(type)
    }

    override fun delete(attachment: Attachment) {
        val context = requireContext()
        val imageView = ImageView(context)
        Glide.with(context)
            .load(attachment.path)
            .into(imageView)

        val positiveAction = DialogInterface.OnClickListener { _, _ ->
            viewModel.deletePermanently(attachment)
        }
        MaterialAlertDialogBuilder(context)
            .setTitle("Are you sure to delete this attachment")
            .setView(imageView)
            .setPositiveButton("Delete", positiveAction)
            .show()
    }

    override fun refresh() {
        adapter.refresh()
    }
}

class AssetsListAdapter(
    private val listener: Listener
) : PagingDataAdapter<Attachment, AssetsListAdapter.ViewHolder>(CALLBACK) {

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
            itemBinding.root.setOnLongClickListener {
                listener.delete(item)
                true
            }
            itemBinding.executePendingBindings()
        }
    }

    interface Listener {
        fun delete(attachment: Attachment)
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