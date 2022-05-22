package me.ikvarxt.halo.ui.posts.post.publish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ikvarxt.halo.databinding.SheetPublishPostBinding
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.ui.posts.post.PostViewModel

@AndroidEntryPoint
class PostPublishSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: SheetPublishPostBinding
    private val viewModel: PostViewModel by viewModels({ requireParentFragment() })
    private val publishViewModel: PostPublishSheetViewModel by viewModels()

    private lateinit var tagsAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetPublishPostBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.apply {
                titleEdit.setText(state.title)
                val slug = if (viewModel.isWritingMode) {
                    // new post, direct generate slug
                    publishViewModel.generateSlug(state.title)
                } else if (!viewModel.post?.title.equals(state.title)) {
                    // editing existing post, if title change, genrate slug
                    publishViewModel.generateSlug(state.title)
                } else {
                    // else using old slug
                    viewModel.post?.slug
                }
                slugEdit.setText(slug)
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                publishViewModel.tags.collect { tags ->
                    val adapterList = tags.map { it.name }
                    tagsAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        adapterList
                    )
                    // TODO: may not work
                    binding.editTags.setAdapter(tagsAdapter)
                }
            }
        }

        binding.editTags.setOnFocusChangeListener { v, hasFocus ->
            val multiAutoCompleteTextView =
                v as? MultiAutoCompleteTextView ?: return@setOnFocusChangeListener
            if (hasFocus) {
                multiAutoCompleteTextView.showDropDown()
            } else {
                multiAutoCompleteTextView.dismissDropDown()
            }
        }
        binding.editTags.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        binding.titleEdit.doAfterTextChanged {
            if (it?.isEmpty() == true) {
                binding.titleEditLayout.error = "Post title can not be empty"
            } else {
                binding.titleEditLayout.error = null
            }
        }

        binding.apply {
            publish.setOnClickListener { publishPost() }
            saveDraft.setOnClickListener { publishPost(true) }
        }
    }

    private fun publishPost(isSaveDraft: Boolean = false) {
        val isNewPost = viewModel.isWritingMode
        val title = viewModel.uiStateTitle.trim()

        if (title.isEmpty()) {
            showToast("title can not be empty")
            dismiss()
            return
        }
        val slug = binding.slugEdit.text.toString().trim()
        val content = viewModel.uiStateContent.trim()

        if (isNewPost) {
            publishViewModel.publishPost(title, slug, content = content, isDraft = isSaveDraft)
        } else {
            viewModel.post?.let { publishViewModel.updatePost(it, title, content, slug) }
        }
    }

    companion object {
        const val TAG = "PostPublishSheetFragment"
    }

}