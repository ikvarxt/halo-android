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
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import me.ikvarxt.halo.databinding.FragmentCreatePostBinding
import me.ikvarxt.halo.extentions.showToast
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private lateinit var binding: FragmentCreatePostBinding
    private val viewModel by viewModels<CreatePostViewModel>()

    @Inject
    lateinit var editor: MarkwonEditor

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

        viewModel.result.observe(viewLifecycleOwner) {
            val msg = if (it.isSuccess) {
                "Publish success"
            } else {
                it.exceptionOrNull().toString()
            }
            requireContext().showToast(msg)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}