package me.ikvarxt.halo.ui.posts.post.publish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.DialogCreateTagBinding
import me.ikvarxt.halo.databinding.SheetPublishPostBinding
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.extentions.toSlug
import me.ikvarxt.halo.ui.posts.post.PostViewModel
import me.ikvarxt.halo.ui.posts.post.UiState

@AndroidEntryPoint
class PostPublishSheetFragment : BottomSheetDialogFragment(), MultiSelectedChipAdapter.Listener {

    private lateinit var binding: SheetPublishPostBinding
    private val viewModel: PostViewModel by viewModels({ requireParentFragment() })
    private val publishViewModel: PostPublishSheetViewModel by viewModels()

    private val tagsAdapter by lazy {
        MultiSelectedChipAdapter(MultiSelectedChipAdapter.MultiAddType.ADD_TAG, this)
    }
    private val categoriesAdapter by lazy {
        MultiSelectedChipAdapter(MultiSelectedChipAdapter.MultiAddType.ADD_CATEGORY, this)
    }

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
                    state.title.toSlug()
                } else if (!viewModel.post?.title.equals(state.title)) {
                    // editing existing post, if title change, generate slug
                    state.title.toSlug()
                } else {
                    // else using old slug
                    viewModel.post?.slug
                }
                slugEdit.setText(slug)
            }
            (state as? UiState.LoadPostUiState)?.post?.let { }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                publishViewModel.tags.collect { tags ->
                    val selectedItem = viewModel.post?.tags?.map { it.id } ?: emptyList()
                    tagsAdapter.submitList(tags, selectedItem, binding.selectTags)
                }
            }
            launch {
                publishViewModel.categories.collect { categories ->
                    val selectedItem = viewModel.post?.categories?.map { it.id } ?: emptyList()
                    categoriesAdapter.submitList(categories, selectedItem, binding.selectCategories)
                }
            }

        }

        binding.titleEdit.doAfterTextChanged {
            if (it?.isEmpty() == true) {
                binding.titleEditLayout.error = "Post title can not be empty"
            } else {
                binding.titleEditLayout.error = null
            }
        }

        binding.apply {
            // FIXME: this cause published post is draft
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
            publishViewModel.publishPost(
                title = title,
                slug = slug,
                content = content,
                isDraft = isSaveDraft,
                categories = categoriesAdapter.selectedItem,
                tags = tagsAdapter.selectedItem
            )
        } else {
            viewModel.post?.let { currentPost ->
                publishViewModel.updatePost(
                    currentPost,
                    title,
                    content,
                    slug,
                    isDraft = isSaveDraft,
                    tagsIds = tagsAdapter.selectedItem,
                    categories = categoriesAdapter.selectedItem
                )
            }
        }
    }

    companion object {
        const val TAG = "PostPublishSheetFragment"
    }

    override fun addNewItem(type: MultiSelectedChipAdapter.MultiAddType) {
        when (type) {
            MultiSelectedChipAdapter.MultiAddType.ADD_TAG -> addTag()
            MultiSelectedChipAdapter.MultiAddType.ADD_CATEGORY -> addCategory()
        }
    }

    private fun createTagOrCategoryDialog(
        title: String,
        positiveListener: (binding: DialogCreateTagBinding) -> Unit
    ) {
        val context = requireContext()
        val binding = DialogCreateTagBinding.inflate(LayoutInflater.from(context), null, false)
        binding.titleEdit.doAfterTextChanged {
            binding.slugEdit.setText(it.toString().toSlug())
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(binding.root)
            .setPositiveButton("Create") { _, _ -> positiveListener(binding) }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun addTag() = createTagOrCategoryDialog("Create Tag") { binding ->
        val name = binding.titleEdit.text.toString().trim()
        val slug = binding.slugEdit.text.toString().trim()
        publishViewModel.createTag(name, slug)
    }

    private fun addCategory() = createTagOrCategoryDialog("Create Category") { binding ->
        val name = binding.titleEdit.text.toString().trim()
        val slug = binding.slugEdit.text.toString().trim()
        publishViewModel.createCategory(name, slug)
    }

}