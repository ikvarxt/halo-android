package me.ikvarxt.halo.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ikvarxt.halo.account.AccountManager
import me.ikvarxt.halo.databinding.ActivityLoginBinding
import me.ikvarxt.halo.extentions.showToast
import me.ikvarxt.halo.ui.MainActivity
import okhttp3.HttpUrl
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var accountManager: AccountManager

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (accountManager.hasActiveAccount()) {
            dropIntoMainActivity()
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ActivityLoginBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!accountManager.accessKeyValidation()) viewModel.refreshToken()

        binding.siteAddress.doAfterTextChanged {
            val input = it.toString()
            val editText = binding.siteAddress
            if (input.isNotBlank()) {
                try {
                    HttpUrl.parse(input)!!.host()
                } catch (e: Exception) {
                    e.printStackTrace()
                    editText.error = "Please input correct https address"
                }
                if (input.startsWith("http://")) {
                    editText.error = "Please input https:// schema"
                }
            } else {
                editText.error = "Site Address can not be empty"
            }
        }

        binding.login.setOnClickListener { loginButtonClick() }

        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.dropToMainScreen.collectLatest {
                    if (it) dropIntoMainActivity()
                }
            }
            launch {
                viewModel.errorOccurred.collectLatest {
                    if (it.isNotBlank() && it.isNotEmpty()) showToast(it)
                }
            }
            launch {
                viewModel.loading.collectLatest {
                    loading(it)
                }
            }
            launch {
                viewModel.refreshTokenState.collectLatest {
                    when (it.isSuccess) {
                        true -> dropIntoMainActivity()
                        false -> {}
                    }
                    loading(it.isRefreshing)
                }
            }
            launch {
                viewModel.loginState.collectLatest {
                    if (it.isLoginSuccess) {
                        dropIntoMainActivity()
                        return@collectLatest
                    } else {
                        it.errorMsg?.let { it1 -> showToast(it1) }
                    }
                    binding.login.isEnabled = !it.isButtonClicked
                }
            }
        }
    }

    private fun loginButtonClick() {
        val domain = binding.siteAddress.text.toString()
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        if (domain.isBlank() && username.isBlank() && password.isBlank()) {
            showToast("Three field must NOT be empty!")
        } else {
            viewModel.login(domain, username, password)
        }
    }

    private fun dropIntoMainActivity() {
        val activity = requireActivity()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity.finish()
    }

    private fun loading(isLoading: Boolean) {
        with(binding) {
            loginProgress.isVisible = isLoading
            loginFormGroup.isVisible = !isLoading
        }
    }
}