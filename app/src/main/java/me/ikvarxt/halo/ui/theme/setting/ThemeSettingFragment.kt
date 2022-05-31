package me.ikvarxt.halo.ui.theme.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.FragmentThemeSettingBinding
import me.ikvarxt.halo.ui.theme.ThemeViewModel

@AndroidEntryPoint
class ThemeSettingFragment : Fragment() {

    private lateinit var binding: FragmentThemeSettingBinding
    private lateinit var adapter: ThemeSettingsAdapter
    private val viewModel: ThemeViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentThemeSettingBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = ThemeSettingsAdapter()

        binding.recyclerView.adapter = adapter

        viewModel.themeConfigs.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.themeSettings.observe(viewLifecycleOwner) { settings ->
            adapter.setupValues(settings)
        }
    }
}