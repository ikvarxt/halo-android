package me.ikvarxt.halo.network.infra

/**
 * class that represents result from network api response, worked with retrofit api request
 */
sealed class NetworkResult<T> {

    data class Success<T>(val data: T, val code: Int = 200) : NetworkResult<T>()

    data class Failure<T>(val code: Int, val msg: String? = null, val e: Throwable? = null) :
        NetworkResult<T>()
}

class NetworkError(msg: String? = null) : Exception(msg)
