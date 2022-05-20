package me.ikvarxt.halo.ui.posts.post.publish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.SheetPublishPostBinding
import me.ikvarxt.halo.ui.posts.post.PostViewModel

@AndroidEntryPoint
class PostPublishSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: SheetPublishPostBinding
    private val viewModel: PostViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetPublishPostBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    companion object {
        const val TAG = "PostPublishSheetFragment"
    }

}