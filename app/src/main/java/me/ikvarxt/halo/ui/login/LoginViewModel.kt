package me.ikvarxt.halo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.ikvarxt.halo.account.AccountManager
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.network.LoginRequestBody
import me.ikvarxt.halo.network.AdminApiService
import me.ikvarxt.halo.network.infra.NetworkResult
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val apiService: AdminApiService,
    private val database: HaloDatabase,
) : ViewModel() {
    private val _errorOccurred = MutableStateFlow("")
    val errorOccurred: StateFlow<String> = _errorOccurred.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _refreshTokenState = MutableStateFlow(RefreshTokenUiState(false, false))
    val refreshTokenState = _refreshTokenState.asStateFlow()

    private val _dropToMainScreen = MutableStateFlow(false)
    val dropToMainScreen = _dropToMainScreen.asStateFlow()

    private val _loginState = MutableStateFlow(LoginUiState(false, false, null))
    val loginState = _loginState.asStateFlow()

    fun login(
        domain: String,
        username: String,
        password: String,
        authcode: String? = null
    ) {
        // TODO: domain validation
        viewModelScope.launch {
            _loading.emit(true)
            _loginState.emit(LoginUiState(true, false, null))

            accountManager.saveDomain(domain)

            val requestBody = LoginRequestBody(username, password, authcode)

            val result = apiService.login(requestBody)
            _loading.emit(false)

            when (result) {
                is NetworkResult.Success -> {
                    accountManager.saveLoginToken(result.data)
                    _loginState.emit(LoginUiState(false, true, null))
                }
                is NetworkResult.Failure -> with(result) {
                    _loginState.emit(LoginUiState(false, false, msg))
                }
            }
        }
    }

    fun precheck(username: String, password: String, authcode: String?): LiveData<Boolean> {
        val resLiveData = MutableLiveData<Boolean>()

        viewModelScope.launch {
            val requestBody = LoginRequestBody(username, password, authcode)
            val precheckResult = apiService.loginPrecheck(body = requestBody)

            when (precheckResult) {
                is NetworkResult.Success -> {
                    resLiveData.value = precheckResult.data.needMFACode
                }
                is NetworkResult.Failure -> resLiveData.value = false
            }
        }

        return resLiveData
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.logout()
            database.clearAllTables()
            apiService.logout()
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            _refreshTokenState.emit(RefreshTokenUiState(true, false))
            if (!accountManager.hasActiveAccount() && accountManager.refreshAccessKey != null) {
                val result = apiService.refreshToken(accountManager.refreshAccessKey!!)

                when (result) {
                    is NetworkResult.Success -> {
                        accountManager.saveLoginToken(result.data)
                        if (accountManager.accessKeyValidation()) {
                            _refreshTokenState.emit(RefreshTokenUiState(false, true))
                            return@launch
                        } else {
                            _errorOccurred.emit("token check failed")
                        }
                    }
                    is NetworkResult.Failure -> error(result.msg, result.code)
                }
            }
            _refreshTokenState.emit(RefreshTokenUiState(false, false))
        }
    }

    private suspend fun error(msg: String?, code: Int) {
        _errorOccurred.emit("Error: code $code $msg")
    }
}

data class LoginUiState(
    val isButtonClicked: Boolean,
    val isLoginSuccess: Boolean,
    val errorMsg: String?
)

data class RefreshTokenUiState(
    val isRefreshing: Boolean,
    val isSuccess: Boolean
)