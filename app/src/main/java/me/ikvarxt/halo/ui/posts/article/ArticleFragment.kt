package me.ikvarxt.halo.ui.posts.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import me.ikvarxt.halo.databinding.FragmentArticleBinding
import me.ikvarxt.halo.network.infra.Status

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val args by navArgs<ArticleFragmentArgs>()
    private val viewModel by viewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPostId(args.postId)

        viewModel.postDetails.observe(viewLifecycleOwner) {
            if (it.status == Status.LOADING) {
                binding.loading.show()
                return@observe
            } else {
                binding.loading.hide()
            }
            when (it.status) {
                Status.SUCCESS -> {
                    val actionBar = (activity as AppCompatActivity).supportActionBar
                    actionBar?.title = it.data?.title

                    val markwon = Markwon.create(requireContext())
                    it.data?.originalContent?.let { it1 ->
                        markwon.setMarkdown(binding.mainArticleText, it1)
                    }
                }
                Status.ERROR -> {
                    binding.errorText.apply {
                        text = it.message
                        visibility = View.VISIBLE
                    }
                }
                else -> {}
            }
        }
    }
}