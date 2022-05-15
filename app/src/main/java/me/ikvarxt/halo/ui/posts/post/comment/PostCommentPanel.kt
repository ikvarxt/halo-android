package me.ikvarxt.halo.ui.posts.post.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import me.ikvarxt.halo.databinding.SheetPostCommentPanelBinding
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle

class PostCommentPanel : BottomSheetDialogFragment() {

    private lateinit var binding: SheetPostCommentPanelBinding
    private val viewModel by activityViewModels<CommentPanelViewModel>()
    private lateinit var adapter: PostCommentPanelAdapter

    private val postId by lazy {
        arguments?.getInt(POST_ID_TAG) ?: 0
    }

    private val highlightId by lazy {
        arguments?.getInt(HIGHLIGHT_ID) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SheetPostCommentPanelBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PostCommentPanelAdapter()

        launchAndRepeatWithViewLifecycle {
            viewModel.getCommentList(postId).collectLatest { list ->
                // TODO: current solving; add loading state later
                binding.emptyLayout.isVisible = list.isEmpty()
                if (list.isNotEmpty()) {
                    val highlight = list.firstOrNull { it.id == highlightId }

                    highlight?.let { it.isHighlight = true }

                    adapter.submitList(list)

                    if (highlight != null) {
                        // FIXME: doesn't work
                        binding.recyclerView.smoothScrollToPosition(list.indexOf(highlight))
                    }
                }
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        const val TAG = "PostCommentPanel"

        const val POST_ID_TAG = "post_id"
        const val HIGHLIGHT_ID = "highlight_id"

        fun newInstance(postId: Int, checkId: Int?): PostCommentPanel {
            return PostCommentPanel().apply {
                arguments = Bundle().apply {
                    putInt(POST_ID_TAG, postId)
                    checkId?.let { putInt(HIGHLIGHT_ID, it) }
                }
            }
        }
    }
}