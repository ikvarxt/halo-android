package me.ikvarxt.halo.ui.posts.post.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import me.ikvarxt.halo.databinding.FragmentPostEditingBinding
import me.ikvarxt.halo.extentions.asMdImage
import me.ikvarxt.halo.ui.posts.post.PostViewModel
import me.ikvarxt.halo.ui.posts.post.UiState
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class PostEditingFragment : Fragment() {

    @Inject
    lateinit var editor: MarkwonEditor

    private lateinit var binding: FragmentPostEditingBinding
    private val viewModel: PostViewModel by viewModels({ requireParentFragment() })

    private val titleEditText: String
        get() = binding.titleEdit.text.toString().trim()

    private val contentEditText: String
        get() = binding.contentEdit.text.toString().trim()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPostEditingBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding.contentEdit) {
            val executors = Executors.newCachedThreadPool()
            val textWatcher = MarkwonEditorTextWatcher.withPreRender(editor, executors, this)
            binding.contentEdit.addTextChangedListener(textWatcher)
        }

        // first load state of editing page
        val uiStateOnceObserver = object : Observer<UiState> {
            override fun onChanged(t: UiState) {
                binding.titleEdit.setText(t.title)
                binding.contentEdit.setText(t.content)
                viewModel.uiState.removeObserver(this)
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, uiStateOnceObserver)

        // listener to trigger changes
        binding.titleEdit.doAfterTextChanged {
            val title = it.toString().trim()
            viewModel.editing(UiState.EditingUiState(title, contentEditText))
        }
        binding.contentEdit.doAfterTextChanged {
            val content = it.toString().trim()
            viewModel.editing(UiState.EditingUiState(titleEditText, content))
        }

        // fragment result for add assets
        setFragmentResultListener(INSERT_ASSET_REQUEST_KEY) { _, bundle ->
            val assetSchema = bundle.getString(ASSET_KEY) ?: return@setFragmentResultListener
            val assetName = bundle.getString(ASSET_NAME_KEY)

            val text = assetSchema.asMdImage(assetName)
            binding.contentEdit.text?.insert(binding.contentEdit.selectionStart, text)
        }
    }

    companion object {
        const val INSERT_ASSET_REQUEST_KEY = "insert_asset"
        const val ASSET_KEY = "asset_key"
        const val ASSET_NAME_KEY = "asset_name_key"
    }
}