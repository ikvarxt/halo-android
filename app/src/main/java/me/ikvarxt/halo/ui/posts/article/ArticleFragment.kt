package me.ikvarxt.halo.ui.posts.article

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.tiagohm.markdownview.css.styles.Github
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.yield
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
                    // TODO: current resolution of markdown support
                    val cssSheet = Github()
                    // make the markdown body with 0 padding, the default padding is too big
                    cssSheet.addRule("body",  "padding: 0px")
                    binding.mainArticleText.addStyleSheet(cssSheet)
                    binding.mainArticleText.loadMarkdown(
                        it.data?.originalContent
                    )
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