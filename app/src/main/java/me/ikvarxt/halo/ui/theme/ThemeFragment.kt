package me.ikvarxt.halo.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.FragmentThemeBinding
import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.network.infra.NetworkResult

@AndroidEntryPoint
class ThemeFragment : Fragment(), ThemeListAdapter.Listener {

    private lateinit var binding: FragmentThemeBinding
    private lateinit var adapter: ThemeListAdapter
    private val viewModel by viewModels<ThemeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentThemeBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ThemeListAdapter(this)

        binding.recyclerView.adapter = adapter

        viewModel.themes.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> adapter.submitList(it.data)
                is NetworkResult.Failure -> showToast("Loading themes error occurred")
            }
        }
    }

    override fun selectActivate(theme: HaloTheme) {
        viewModel.activateTheme(theme)
    }
}