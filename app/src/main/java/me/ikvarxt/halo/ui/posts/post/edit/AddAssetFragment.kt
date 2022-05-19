package me.ikvarxt.halo.ui.posts.post.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import me.ikvarxt.halo.databinding.SheetAddAssetBinding
import me.ikvarxt.halo.entites.Attachment
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.ui.assets.AssetsListAdapter
import me.ikvarxt.halo.ui.assets.AssetsViewModel

@AndroidEntryPoint
class AddAssetFragment : BottomSheetDialogFragment(), AssetsListAdapter.Listener {

    private lateinit var binding: SheetAddAssetBinding
    private val viewModel by viewModels<AssetsViewModel>()
    private lateinit var adapter: AssetsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetAddAssetBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AssetsListAdapter(this)

        binding.recyclerView.adapter = adapter

        launchAndRepeatWithViewLifecycle {
            viewModel.attachments.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun delete(attachment: Attachment) {}

    override fun chooseInsert(attachment: Attachment) {
        setFragmentResult(
            PostEditingFragment.INSERT_ASSET_REQUEST_KEY,
            bundleOf(PostEditingFragment.ASSET_KEY to attachment.path)
        )
        dismiss()
    }

    companion object {
        const val TAG = "AddAssetFragment"
    }
}