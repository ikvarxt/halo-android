package me.ikvarxt.halo.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentDashboardBinding
import me.ikvarxt.halo.ui.MainViewModel
import me.ikvarxt.halo.ui.login.LoginActivity
import me.ikvarxt.halo.ui.login.LoginViewModel

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    private val loginViewModel by viewModels<LoginViewModel>()
    private val parentViewModel by activityViewModels<MainViewModel>()

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

        parentViewModel.infoLiveData.observe(viewLifecycleOwner) { profile ->
            binding.profile = profile
            binding.executePendingBindings()
        }

        binding.logoutBtn.setOnClickListener { logout() }
        binding.themeSetting.setOnClickListener {
            findNavController().navigate(R.id.themeFragment)
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
}