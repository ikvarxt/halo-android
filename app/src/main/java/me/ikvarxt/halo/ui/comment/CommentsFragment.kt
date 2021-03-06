package me.ikvarxt.halo.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.databinding.DialogReplyToCommentBinding
import me.ikvarxt.halo.databinding.FragmentCommentBinding
import me.ikvarxt.halo.entites.PostComment
import me.ikvarxt.halo.extentions.editWithMarkdown
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.ui.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CommentsFragment : Fragment(), PostsCommentsAdapter.Listener {

    @Inject
    lateinit var markwon: Markwon

    @Inject
    lateinit var editor: MarkwonEditor

    private lateinit var binding: FragmentCommentBinding
    private lateinit var adapter: PostsCommentsAdapter
    private val viewModel by viewModels<CommentViewModel>()
    private val parentViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCommentBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PostsCommentsAdapter(this, markwon)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.pastsComments.collectLatest {
                    adapter.submitData(it)
                }
            }
            launch {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    binding.swipeRefreshLayout.isRefreshing =
                        loadStates.mediator?.refresh is LoadState.Loading
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        viewModel.message.observe(viewLifecycleOwner) { msg ->
            showToast(msg)
        }
    }

    override fun replyTo(commentItem: PostComment) {
        val context = requireContext()
        val binding = DialogReplyToCommentBinding.inflate(layoutInflater, null, false)

        fun positiveClick() {
            if (parentViewModel.info != null) {
                viewModel.replyComment(
                    commentItem,
                    parentViewModel.info!!,
                    binding.editText.text.toString()
                )
            } else {
                showToast("Can not get user profile")
            }
        }

        binding.editText.editWithMarkdown(editor)

        MaterialAlertDialogBuilder(context)
            .setTitle("reply to ${commentItem.author}")
            .setPositiveButton("reply") { _, _ ->
                positiveClick()
            }
            .setView(binding.root)
            .show()
    }

    override fun delete(commentItem: PostComment) {
        val context = requireContext()

        fun positiveClick() {
            viewModel.delete(commentItem)
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Are you sure to delete this comment?")
            .setPositiveButton("Delete") { _, _ ->
                positiveClick()
            }
            .show()
    }

    override fun goToPostComment(commentItem: PostComment) {
        val postId = commentItem.post?.id ?: return

        val action = CommentsFragmentDirections.gotoPostCommentList(postId, commentItem.id)
        findNavController().navigate(action)
    }
}