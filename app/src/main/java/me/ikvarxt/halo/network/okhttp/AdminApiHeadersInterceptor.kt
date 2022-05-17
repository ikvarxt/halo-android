package me.ikvarxt.halo.network.okhttp

import me.ikvarxt.halo.account.AccountManager
import me.ikvarxt.halo.di.NetworkModule
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class AdminApiHeadersInterceptor(
    private val accountManager: AccountManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        return if (original.url().host().equals(NetworkModule.PLACEHOLDER_HOST)) {
            val request = original.newBuilder()

            val host = accountManager.domain
            if (host?.isNotBlank() != null) {
                request.url(swapHost(original.url(), host))
            }
            accountManager.accessKey?.let {
                request.addHeader(AccountManager.ADMIN_AUTH_HEADER_NAME, it)
            }
            chain.proceed(request.build())
        } else {
            chain.proceed(original)
        }
    }

    companion object {
        private fun swapHost(url: HttpUrl, host: String): HttpUrl {
            return url.newBuilder().host(host).build()
        }
    }
}