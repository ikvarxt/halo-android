package me.ikvarxt.halo.network.okhttp

import me.ikvarxt.halo.network.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ContentApiHeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader(Constants.CONTENT_ACCESS_KEY_HEADER__PARAM_NAME, Constants.accessKey)
            .build()
        return chain.proceed(request)
    }
}