package me.ikvarxt.halo.ui.posts.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
 import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import me.ikvarxt.halo.databinding.FragmentCreatePostBinding
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.network.infra.NetworkResult
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    @Inject
    lateinit var editor: MarkwonEditor

    private lateinit var binding: FragmentCreatePostBinding
    private val viewModel by viewModels<CreatePostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCreatePostBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar(binding.toolbar)

        binding.apply {
            contentInput.addTextChangedListener(
                MarkwonEditorTextWatcher.withPreRender(
                    editor,
                    Executors.newCachedThreadPool(),
                    contentInput
                )
            )
        }

        binding.postTitleInput.addTextChangedListener {
            viewModel.title = it.toString()
        }

        binding.publishFab.setOnClickListener {
            if (viewModel.title.isBlank()) {
                it.context.showToast("title can not be empty")
            } else {
                viewModel.publishPost(binding.contentInput.text.toString())
            }
        }

        viewModel.result.observe(viewLifecycleOwner) {result->
            val msg= when (result) {
                is NetworkResult.Success -> {
                    "Post ${result.data.id} published success"
                }
                is NetworkResult.Failure -> {
                    "error occurred: code ${result.code}, ${result.msg}"
                }
                is NetworkResult.NetworkError -> {
                    "Network Error"
                }
            }

            requireContext().showToast(msg)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}