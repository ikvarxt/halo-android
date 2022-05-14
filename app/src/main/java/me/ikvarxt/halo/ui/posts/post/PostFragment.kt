package me.ikvarxt.halo.ui.posts.post

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.collectLatest
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentPostBinding
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.ui.MainActivity
import me.ikvarxt.halo.ui.posts.post.comment.PostCommentPanel
import javax.inject.Inject

@AndroidEntryPoint
class PostFragment : Fragment() {

    @Inject
    lateinit var markwon: Markwon

    private lateinit var binding: FragmentPostBinding
    private val args by navArgs<PostFragmentArgs>()
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPostBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.setPostId(args.postId)

        viewModel.postDetails.observe(viewLifecycleOwner) {
            loadContentData(it)
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.loading.collectLatest {
                when (it) {
                    true -> {
                        binding.loading.show()
                    }
                    false -> {
                        binding.loading.hide()
                    }
                }
            }
        }
    }

    private fun loadContentData(result: NetworkResult<PostDetails>) {
        val activity = activity as MainActivity

        fun setupContent(data: PostDetails) {
            activity.supportActionBar?.title = data.title

            data.originalContent?.let { content ->
                markwon.setMarkdown(binding.mainArticleText, content)
            }
        }

        when (result) {
            is NetworkResult.Success -> {
                val data = result.data
                setupContent(data)
            }
            is NetworkResult.Failure -> {
                binding.errorText.apply {
                    text = result.msg
                    visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.showComment -> {
                PostCommentPanel.newInstance(args.postId)
                    .show(childFragmentManager, PostCommentPanel.TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}