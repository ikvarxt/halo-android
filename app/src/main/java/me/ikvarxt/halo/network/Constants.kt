package me.ikvarxt.halo.network

import android.content.Context
import me.ikvarxt.halo.application

object Constants {

    private const val DOMAIN = "domain"
    private const val ACCESS_KEY = "access_key"

    var domain: String
        set(value) = getSp().edit().putString(DOMAIN, value).apply()
        get() = getSp().getString(DOMAIN, "")!!

    var accessKey: String
        set(value) = getSp().edit().putString(ACCESS_KEY, value).apply()
        get() = getSp().getString(ACCESS_KEY, "")!!

    private fun getSp() = application.getSharedPreferences("test", Context.MODE_PRIVATE)
}