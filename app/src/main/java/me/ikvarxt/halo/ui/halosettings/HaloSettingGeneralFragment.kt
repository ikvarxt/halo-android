package me.ikvarxt.halo.ui.halosettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.R
import me.ikvarxt.halo.constant.HaloSettingsConstants.General
import me.ikvarxt.halo.extentions.applyAppbarMargin

@AndroidEntryPoint
class HaloSettingGeneralFragment : PreferenceFragmentCompat() {

    private val viewModel by activityViewModels<HaloSettingsViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.halo_setting_general, rootKey)

        findPreference<EditTextPreference>(General.BLOG_TITLE)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.saveOption(General.BLOG_TITLE, newValue as String)
                true
            }
        }
        findPreference<EditTextPreference>(General.BLOG_URL)?.apply {
//            setupEditTextPreferences(viewModel.blogUrl)
        }
        findPreference<EditTextPreference>(General.BLOG_LOGO)?.apply {
//            setupEditTextPreferences(viewModel.logo)
        }
        findPreference<EditTextPreference>(General.FAVICON)?.apply {
//            setupEditTextPreferences(viewModel.favicon)
        }
        findPreference<EditTextPreference>(General.FOOTER)?.apply {
//            setupEditTextPreferences(viewModel.footer)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            applyAppbarMargin()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.optionsLiveData.observe(viewLifecycleOwner) {
            it.map { (key, value) ->
                when (key) {
                    General.BLOG_TITLE,
                    General.BLOG_URL,
                    General.BLOG_LOGO,
                    General.FAVICON,
                    General.FOOTER -> {
                        findPreference<EditTextPreference>(key)?.apply {
                            setupEditTextPreferences(value)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun EditTextPreference.setupEditTextPreferences(value: String) {
        summary = value
        text = value
    }

}