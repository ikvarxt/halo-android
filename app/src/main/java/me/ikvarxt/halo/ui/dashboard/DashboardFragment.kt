package me.ikvarxt.halo.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentDashboardBinding
import me.ikvarxt.halo.extentions.launchAndRepeatWithViewLifecycle
import me.ikvarxt.halo.extentions.showErrorToast
import me.ikvarxt.halo.ui.MainViewModel
import me.ikvarxt.halo.ui.login.LoginActivity
import me.ikvarxt.halo.ui.login.LoginViewModel

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    private val loginViewModel by viewModels<LoginViewModel>()
    private val parentViewModel by activityViewModels<MainViewModel>()

    private var blogUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDashboardBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchAndRepeatWithViewLifecycle {
            launch {
                parentViewModel.profile.collectLatest {
                    binding.profile = it.firstOrNull()
                }
            }
            launch {
                parentViewModel.blogUrl.collectLatest { url ->
                    url?.let {
                        blogUrl = url.value
                    }
                }
            }
        }

        binding.descriptionText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                parentViewModel.updateDescription(v.text.toString().trim())
                true
            } else {
                false
            }

        }

        binding.logoutBtn.setOnClickListener { logout() }
        binding.themeSetting.setOnClickListener {
            findNavController().navigate(R.id.gotoThemeSetting)
        }
        binding.assetsManagement.setOnClickListener {
            findNavController().navigate(R.id.gotoAssetsFragment)
        }
        binding.haloSetting.setOnClickListener {
            findNavController().navigate(R.id.gotoHaloSetting)
        }
    }

    private fun logout() {
        loginViewModel.logout()
        binding.logoutBtn.postDelayed({
            val activity = requireActivity()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity.finish()
        }, 500)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.openInBrowser -> openBlogInBrowser()
            else -> {}
        }
        return true
    }

    private fun openBlogInBrowser() {
        if (URLUtil.isValidUrl(blogUrl)) {
            val uri = Uri.parse(blogUrl)
            val tab = CustomTabsIntent.Builder().build()
            tab.launchUrl(requireContext(), uri)
        } else {
            showErrorToast()
        }
    }

}