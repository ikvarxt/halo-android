package me.ikvarxt.halo.utils

import me.ikvarxt.halo.network.infra.NetworkResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                NetworkResult::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    NetworkResultCallAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }

}