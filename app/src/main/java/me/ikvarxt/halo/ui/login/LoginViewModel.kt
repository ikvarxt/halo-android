package me.ikvarxt.halo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.ikvarxt.halo.account.AccountManager
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.network.LoginRequestBody
import me.ikvarxt.halo.entites.network.LoginToken
import me.ikvarxt.halo.entites.network.NeedMFACode
import me.ikvarxt.halo.network.AdminApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val apiService: AdminApiService,
    private val database: HaloDatabase,
) : ViewModel() {

    fun login(
        domain: String,
        username: String,
        password: String,
        authcode: String? = null
    ): LiveData<Boolean> {
        // TODO: domain validation
        accountManager.saveDomain(domain)

        val resLiveData = MutableLiveData<Boolean>()
        val requestBody = LoginRequestBody(username, password, authcode)

        val call = apiService.login(requestBody)

        val callback = object : Callback<LoginToken> {
            override fun onResponse(call: Call<LoginToken>, response: Response<LoginToken>) {
                try {
                    val body = response.body() as LoginToken
                    accountManager.saveLoginToken(body)
                    resLiveData.postValue(true)
                } catch (e: Exception) {
                    response.errorBody()?.let { e.addSuppressed(Exception(it.string())) }
                    e.printStackTrace()
                    resLiveData.postValue(false)
                }
            }

            override fun onFailure(call: Call<LoginToken>, t: Throwable) {
                t.printStackTrace()
                resLiveData.postValue(false)
            }
        }
        call.enqueue(callback)

        return resLiveData
    }

    fun precheck(username: String, password: String, authcode: String?): LiveData<Boolean> {
        val resLiveData = MutableLiveData<Boolean>()

        val requestBody = LoginRequestBody(username, password, authcode)
        val precheckCall = apiService.loginPrecheck(body = requestBody)
        val callback = object : Callback<NeedMFACode> {
            override fun onResponse(call: Call<NeedMFACode>, response: Response<NeedMFACode>) {
                try {
                    val needMFACode = (response.body() as NeedMFACode).needMFACode
                    resLiveData.postValue(needMFACode)
                } catch (e: Exception) {
                    response.errorBody()?.let { e.addSuppressed(Exception(it.string())) }
                    onFailure(call, e)
                }
            }

            override fun onFailure(call: Call<NeedMFACode>, t: Throwable) {
                t.printStackTrace()
                resLiveData.postValue(false)
            }
        }
        precheckCall.enqueue(callback)

        return resLiveData
    }

    fun logout(): LiveData<Boolean> {
        val res = MutableLiveData<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            accountManager.logout()
            database.clearAllTables()
//            apiService.logout().enqueue(object : Callback<Unit> {
//                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                    res.postValue(response.isSuccessful)
//                }
//
//                override fun onFailure(call: Call<Unit>, t: Throwable) {
//                    res.postValue(false)
//                }
//            })
            res.postValue(true)
        }
        return res
    }

    fun refreshToken(): LiveData<Boolean> {
        val res = MutableLiveData<Boolean>()

        if (!accountManager.hasActiveAccount() && accountManager.refreshAccessKey != null) {
            val call = apiService.refreshToken(accountManager.refreshAccessKey!!)

            val callback = object : Callback<LoginToken> {
                override fun onResponse(call: Call<LoginToken>, response: Response<LoginToken>) {
                    if (response.isSuccessful) {
                        val token = response.body() as LoginToken
                        accountManager.saveLoginToken(token)
                        res.postValue(accountManager.accessKeyValidation())
                    } else {
                        res.postValue(false)
                    }
                }

                override fun onFailure(call: Call<LoginToken>, t: Throwable) {
                    t.printStackTrace()
                    res.postValue(false)
                }
            }
            call.enqueue(callback)
        } else {
            res.postValue(false)
        }
        return res
    }
}