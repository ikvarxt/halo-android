package me.ikvarxt.halo.ui.posts.post.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.databinding.FragmentPostPreviewBinding
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.ui.MainActivity
import me.ikvarxt.halo.ui.posts.post.PostViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PostPreviewFragment : Fragment() {

    @Inject
    lateinit var markwon: Markwon

    private lateinit var binding: FragmentPostPreviewBinding
    private val viewModel: PostViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPostPreviewBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            load(it.title, it.content)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorText.text = it
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.loading.collectLatest {
                    if (it) binding.loading.show()
                    else binding.loading.hide()
                }
            }
        }
    }

    private fun loadContentData(data: PostDetails) {
        (activity as MainActivity).toolbar.title = data.title
        binding.errorText.text = null

        data.originalContent?.let { content ->
            markwon.setMarkdown(binding.mainArticleText, content)
        }
    }

    private fun load(title: String, content: String) {
        (activity as MainActivity).toolbar.title = title
        binding.errorText.text = null

        markwon.setMarkdown(binding.mainArticleText, content)
    }
}