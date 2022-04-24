package me.ikvarxt.halo.utils

import me.ikvarxt.halo.network.infra.NetworkResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type

class NetworkResultCallAdapter(private val type: Type) :
    CallAdapter<Type, Call<NetworkResult<Type>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<NetworkResult<Type>> = ResultCall(call)
}

/**
 * from icesmith
 * @see <a href="https://github.com/icesmith">icesmith</a>
 * @see <a href="https://github.com/icesmith/android-samples/blob/master/RetrofitSuspendingCallAdapter/app/src/main/java/com/icesmith/retrofitsuspendingcalladapter/MainVM.kt">
 *     RetrofitSuspendingCallAdapter</a>
 */
private abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = proxy.timeout()

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

private class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, NetworkResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<NetworkResult<T>>) =
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                val result = if (response.isSuccessful) {
                    val body = response.body()!! as T
                    NetworkResult.Success(body, code)
                } else {
                    val errorBody = response.errorBody()!!
                    NetworkResult.Failure(code, errorBody.string(), null)
                }
                callback.onResponse(this@ResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result: NetworkResult<T> = if (t is IOException) {
                    NetworkResult.Failure(-1, "Network Error", t)
                } else {
                    NetworkResult.Failure(-1, null, t)
                }

                callback.onResponse(this@ResultCall, Response.success(result))
            }
        })

    override fun cloneImpl() = ResultCall(proxy.clone())
}
