package me.ikvarxt.halo.ui.posts.post.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentPostEditingBinding
import me.ikvarxt.halo.extentions.asMdImage
import me.ikvarxt.halo.ui.posts.post.PostViewModel
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class PostEditingFragment : Fragment() {

    @Inject
    lateinit var editor: MarkwonEditor

    private lateinit var binding: FragmentPostEditingBinding

    //    private val viewModel by viewModels<CreatePostViewModel>()
    private val viewModel: PostViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPostEditingBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding.contentEdit) {
            val executors = Executors.newCachedThreadPool()
            val textWatcher = MarkwonEditorTextWatcher.withPreRender(editor, executors, this)
            binding.contentEdit.addTextChangedListener(textWatcher)
        }

        viewModel.postLiveData.observe(viewLifecycleOwner) { post ->
            binding.titleEdit.setText(post.title)
            binding.contentEdit.setText(post.originalContent)
        }

//        binding.postTitleEdit.addTextChangedListener {
//            viewModel.title = it.toString()
//        }
//
//        binding.publishFab.setOnClickListener {
//            if (viewModel.title.isBlank()) {
//                showToast("title can not be empty")
//            } else {
//                viewModel.publishPost(binding.contentEdit.text.toString())
//            }
//        }
//
//        viewModel.result.observe(viewLifecycleOwner) { result ->
//            val msg = when (result) {
//                is NetworkResult.Success -> {
//                    "Post ${result.data.id} published success"
//                }
//                is NetworkResult.Failure -> {
//                    "error occurred: code ${result.code}, ${result.msg}"
//                }
//            }
//            showToast(msg)
//        }

        binding.bottomBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.insertAsset -> {
                    AddAssetFragment().show(childFragmentManager, AddAssetFragment.TAG)
                }
                else -> {}
            }
            true
        }

        setFragmentResultListener(INSERT_ASSET_REQUEST_KEY) { _, bundle ->
            val assetSchema = bundle.getString(ASSET_KEY) ?: return@setFragmentResultListener

            val text = assetSchema.asMdImage()
            binding.contentEdit.text?.insert(binding.contentEdit.selectionStart, text)
        }
    }

    companion object {
        const val INSERT_ASSET_REQUEST_KEY = "insert_asset"
        const val ASSET_KEY = "asset_key"
    }
}