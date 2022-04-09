package me.ikvarxt.halo.account

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.ikvarxt.halo.application
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.HaloResponse
import me.ikvarxt.halo.entites.network.LoginRequestBody
import me.ikvarxt.halo.entites.network.LoginToken
import me.ikvarxt.halo.network.AdminApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(val database: HaloDatabase) {

    @Volatile
    var domain: String? = null
        private set

    var accessKey: String?
        set(value) = getSp().edit().putString(ACCESS_KEY, value).apply()
        get() = getSp().getString(ACCESS_KEY, null)

    var refreshAccessKey: String?
        set(value) = getSp().edit().putString(REFRESH_KEY, value).apply()
        get() = getSp().getString(REFRESH_KEY, null)

    var expiredIn: Long
        set(value) = getSp().edit().putLong(EXPIRE_KEY, value).apply()
        get() = getSp().getLong(EXPIRE_KEY, 0L)

    init {
        domain = getSp().getString(DOMAIN, null)
    }

    fun saveDomain(domain: String) {
        this.domain = domain
        getSp().edit().putString(DOMAIN, domain).apply()
    }

    fun login(
        adminApiService: AdminApiService,
        username: String,
        password: String,
        authcode: String? = null
    ): LiveData<Boolean> {
        val resLiveData = MutableLiveData<Boolean>()
        val requestBody = LoginRequestBody(username, password, authcode)

        fun callLogin() {
            val call = adminApiService.login(requestBody)

            call.enqueue(object : Callback<HaloResponse<LoginToken>> {
                override fun onResponse(
                    call: Call<HaloResponse<LoginToken>>,
                    response: Response<HaloResponse<LoginToken>>
                ) {
                    try {
                        val body = response.body() as HaloResponse<LoginToken>
                        accessKey = body.data.accessToken
                        refreshAccessKey = body.data.refreshToken
                        expiredIn = body.data.expiredIn
                        getSp().edit().putLong(ACCESS_CREATE_TIME, System.currentTimeMillis())
                            .apply()
                        resLiveData.postValue(true)
                    } catch (e: Exception) {
                        response.errorBody()?.let { e.addSuppressed(Exception(it.string())) }
                        e.printStackTrace()
                        resLiveData.postValue(false)
                    }
                }

                override fun onFailure(call: Call<HaloResponse<LoginToken>>, t: Throwable) {
                    Log.e(TAG, "onFailure: login", t)
                    t.printStackTrace()
                    resLiveData.postValue(false)
                }
            })
        }

//        val precheckCall = adminApiService.loginPrecheck(body = requestBody)
//        precheckCall.enqueue(object : Callback<HaloResponse<NeedMFACode>> {
//            override fun onResponse(
//                call: Call<HaloResponse<NeedMFACode>>,
//                response: Response<HaloResponse<NeedMFACode>>
//            ) {
//                try {
//                    val needMFACode =
//                        (response.body() as HaloResponse<NeedMFACode>).data.needMFACode
//                    callLogin()
//                } catch (e: Exception) {
//                    response.errorBody()?.let { e.addSuppressed(Exception(it.string())) }
//                    onFailure(call, e)
//                }
//            }
//
//            override fun onFailure(call: Call<HaloResponse<NeedMFACode>>, t: Throwable) {
//
//                t.printStackTrace()
//                resLiveData.postValue(false)
//            }
//        })
        callLogin()

        return resLiveData
    }

    fun checkValidation() = accessKey?.isNotBlank() == true && !isAccessKeyExpired()

    private fun isAccessKeyExpired(): Boolean {
        if (expiredIn > 0) {
            val createdTime = getSp().getLong(ACCESS_CREATE_TIME, 0L)
            return System.currentTimeMillis() - createdTime > expiredIn
        }
        return true
    }

    fun logout(adminApiService: AdminApiService): LiveData<Boolean> {
        val res = MutableLiveData<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            database.clearAllTables()
            getSp().edit().clear().apply()
            adminApiService.logout().enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Log.d(TAG, "onResponse: logout success")
                    res.postValue(true)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d(TAG, "onFailure: logout failed")
                    res.postValue(false)
                }
            })
        }
        return res
    }

    companion object {
        private const val TAG = "AccountManager"
        private const val DOMAIN = "domain"
        private const val ACCESS_KEY = "access_key"
        private const val REFRESH_KEY = "refresh_key"
        private const val EXPIRE_KEY = "expire_key"
        private const val ACCESS_CREATE_TIME = "access_create_time"

        const val ADMIN_AUTH_HEADER_NAME = "ADMIN-Authorization"

        fun refreshToken(): Boolean {
            return false
        }

        fun getSp() = application.getSharedPreferences("test", Context.MODE_PRIVATE)
    }
}