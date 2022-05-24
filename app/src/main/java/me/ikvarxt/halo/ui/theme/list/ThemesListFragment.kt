package me.ikvarxt.halo.ui.theme.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.FragmentThemesListBinding
import me.ikvarxt.halo.entites.HaloTheme
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.network.infra.NetworkResult
import me.ikvarxt.halo.ui.theme.ThemeListAdapter
import me.ikvarxt.halo.ui.theme.ThemeViewModel

@AndroidEntryPoint
class ThemesListFragment : Fragment(), ThemeListAdapter.Listener {

    private lateinit var binding: FragmentThemesListBinding
    private val viewModel: ThemeViewModel by viewModels()
    private lateinit var adapter: ThemeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentThemesListBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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