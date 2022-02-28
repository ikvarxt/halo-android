package me.ikvarxt.halo.utils


import androidx.lifecycle.LiveData
import me.ikvarxt.halo.network.infra.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 * @param <R>
</R> */
class LiveDataCallAdapter<R>(private val responseType: Type) :
// basic use of retrofit CallAdapter
    CallAdapter<R, LiveData<ApiResponse<R>>> {

    override fun responseType() = responseType

    /**
     * I guess based on responseType method, retrofit adapt its response data to this type,
     * than using adapt method to process typed data as the user wanted data type
     */
    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            // TODO: 2/25/22 the main propose of this AtomicBoolean
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    // when have observer observing this livedata, we enqueue this call
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            // ApiResponse factory create used here
                            postValue(ApiResponse.create(response))
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(ApiResponse.create(throwable))
                        }
                    })
                }
            }
        }
    }
}
