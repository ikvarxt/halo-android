package me.ikvarxt.halo.account

import android.content.Context
import me.ikvarxt.halo.application
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.network.LoginToken
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

    fun saveLoginToken(token: LoginToken) {
        accessKey = token.accessToken
        refreshAccessKey = token.refreshToken
        expiredIn = token.expiredIn

        getSp().edit()
            .putLong(ACCESS_CREATE_TIME, System.currentTimeMillis())
            .apply()
    }


    fun logout(): Boolean {
        return getSp().edit().clear().commit()
    }

    fun hasActiveAccount(): Boolean {
        // TODO: domain validation
        return !domain.isNullOrBlank() && accessKeyValidation()
    }

    fun accessKeyValidation() = accessKey?.isNotBlank() == true && !isAccessKeyExpired()

    private fun isAccessKeyExpired(): Boolean {
        return if (refreshAccessKey.isNullOrBlank() && expiredIn == 0L) {
            true
        } else {
            val createdTime = getSp().getLong(ACCESS_CREATE_TIME, 0L)
            System.currentTimeMillis() - createdTime > expiredIn * 100
        }
    }

    companion object {
        private const val TAG = "AccountManager"
        private const val DOMAIN = "domain"
        private const val ACCESS_KEY = "access_key"
        private const val REFRESH_KEY = "refresh_key"
        private const val EXPIRE_KEY = "expire_key"
        const val ACCESS_CREATE_TIME = "access_create_time"

        const val ADMIN_AUTH_HEADER_NAME = "ADMIN-Authorization"

        fun getSp() = application.getSharedPreferences("test", Context.MODE_PRIVATE)
    }
}