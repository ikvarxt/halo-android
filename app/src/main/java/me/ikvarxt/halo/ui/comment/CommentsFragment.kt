package me.ikvarxt.halo.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.databinding.FragmentCommentBinding
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle

@AndroidEntryPoint
class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentBinding
    private lateinit var adapter: PostsCommentsAdapter
    private val viewModel by viewModels<CommentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCommentBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PostsCommentsAdapter()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.pastsComments.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }
}