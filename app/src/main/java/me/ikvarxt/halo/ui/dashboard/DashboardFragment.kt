package me.ikvarxt.halo.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.ikvarxt.halo.R
import me.ikvarxt.halo.databinding.FragmentDashboardBinding
import me.ikvarxt.halo.network.Constants
import me.ikvarxt.halo.ui.login.LoginActivity

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

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
            Constants.getSp().edit()
                .clear().apply()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
//            Toast.makeText(this, "domain and access key data has been cleared", Toast.LENGTH_SHORT)
        }
    }
}