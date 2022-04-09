package me.ikvarxt.halo.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import me.ikvarxt.halo.account.AccountManager
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.databinding.FragmentDashboardBinding
import me.ikvarxt.halo.network.AdminApiService
import me.ikvarxt.halo.ui.login.LoginActivity
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    @Inject
    lateinit var accountManager: AccountManager

    @Inject
    lateinit var database: HaloDatabase

    @Inject
    lateinit var adminApiService: AdminApiService

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
            accountManager.logout(adminApiService).observe(viewLifecycleOwner) {
                if (it) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(context, "something went wrong when logout", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}