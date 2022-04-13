package me.ikvarxt.halo.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.databinding.FragmentDashboardBinding
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.ui.login.LoginActivity
import me.ikvarxt.halo.ui.login.LoginViewModel

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    private val loginViewModel by viewModels<LoginViewModel>()

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

        binding.logout.setOnClickListener {
            loginViewModel.logout().observe(viewLifecycleOwner) {
                if (it) {
                    startActivity(Intent(requireContext(),LoginActivity::class.java))
                    requireActivity().finish()
                } else {
                    requireContext().showToast("logout failed")
                }
            }
        }
    }
}