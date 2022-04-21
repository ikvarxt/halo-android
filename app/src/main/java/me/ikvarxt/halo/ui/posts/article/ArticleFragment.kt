package me.ikvarxt.halo.ui.posts.article

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import me.ikvarxt.halo.databinding.FragmentArticleBinding
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.network.infra.Resource
import me.ikvarxt.halo.network.infra.Status
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
    ): View {
        binding = FragmentArticleBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPostId(args.postId)

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewModel.postDetails.observe(viewLifecycleOwner) {
            loadContentData(it)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadContentData(resource: Resource<PostDetails>) {
        fun setupContent(data: PostDetails) {
            binding.toolbarLayout.title = data.title

            val thumbnail = data.thumbnail
            if (thumbnail != null && thumbnail.isNotBlank()) {
                binding.headerImage.isVisible = true
                Glide.with(binding.headerImage.context)
                    .asDrawable()
                    .load(Uri.parse(thumbnail))
                    .centerCrop()
                    .into(binding.headerImage)
            } else {
            }

            data.originalContent.let { content ->
                markwon.setMarkdown(binding.mainArticleText, content)
            }
        }

        if (resource.status == Status.LOADING) {
            binding.loading.show()
            if (resource.data != null) setupContent(resource.data)
            return
        } else {
            binding.loading.hide()
        }
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data!!
                setupContent(data)
            }
            Status.ERROR -> {
                binding.errorText.apply {
                    text = resource.message
                    visibility = View.VISIBLE
                }
            }
            else -> {}
        }
    }
}