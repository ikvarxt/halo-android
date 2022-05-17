package me.ikvarxt.halo.ui.posts.post

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.browser.customtabs.CustomTabsIntent
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

        viewModel.postLiveData.observe(viewLifecycleOwner) {
            loadContentData(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorText.text = it
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

        if (args.highlightId != NO_HIGHLIGHT_ID) {
            showCommentList(args.highlightId)
        }
    }

    private fun loadContentData(data: PostDetails) {
        val activity = activity as MainActivity

        fun setupContent(data: PostDetails) {
            activity.supportActionBar?.title = data.title
            binding.errorText.text = null

            data.originalContent?.let { content ->
                markwon.setMarkdown(binding.mainArticleText, content)
            }
        }

        setupContent(data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.showComment -> {
                showCommentList()
            }
            R.id.openInBrowser -> {
                // TODO: add draft post checking
                val customTabIntent = CustomTabsIntent.Builder().build()
                viewModel.post?.fullPath?.let { path ->
                    customTabIntent.launchUrl(requireContext(), Uri.parse(path))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCommentList(highlightId: Int? = null) {
        PostCommentPanel.newInstance(args.postId, highlightId)
            .show(childFragmentManager, PostCommentPanel.TAG)
    }

    companion object {
        const val NO_HIGHLIGHT_ID = -1
    }
}