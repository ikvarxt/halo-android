package me.ikvarxt.halo.ui.posts.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.collectLatest
import me.ikvarxt.halo.databinding.FragmentArticleBinding
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    @Inject
    lateinit var markwon: Markwon

    private lateinit var binding: FragmentArticleBinding
    private val args by navArgs<ArticleFragmentArgs>()
    private val viewModel by viewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentArticleBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPostId(args.postId)

        val activity = activity as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
}