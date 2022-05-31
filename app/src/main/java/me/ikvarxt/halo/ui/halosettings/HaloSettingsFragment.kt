package me.ikvarxt.halo.ui.halosettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.R
import me.ikvarxt.halo.constant.HaloSettingsConstants
import me.ikvarxt.halo.extentions.applyAppbarMargin

@AndroidEntryPoint
class HaloSettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by activityViewModels<HaloSettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: current workaround
        viewModel.options
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.halo_setting, rootKey)

        findPreference<Preference>(HaloSettingsConstants.GROUP_GENERAL)?.apply {
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.gotoGeneralSetting)
                true
            }
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

}