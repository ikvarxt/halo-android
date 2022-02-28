package me.ikvarxt.halo.utils

import androidx.lifecycle.LiveData
import me.ikvarxt.halo.network.infra.ApiResponse
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * factory to process retrofit response from api to user given return type,
 * this case return a livedata wrapped ApiResponse
 */
/**
 * main propose is to check user defined service method is correct or not,
 * than process the return data as wrapped ApiResponse
 */
class LiveDataCallAdapterFactory : Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // if user set return type not livedata, just return. this case uses all livedata returns
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        // retrive wrapped type inside livedata
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        // get the raw type of ApiResponse inside livedata, at least in this case
        /**
         * getRawType returns the raw type of generic type,
         * such as List<? extends Runnable> returns List.class
         */
        val rawObservableType = getRawType(observableType)
        if (rawObservableType != ApiResponse::class.java) {
            throw IllegalArgumentException("type must be a resource")
        }
        // make sure that ApiResponse is a parameterized type that contains real data
        // which have generic type contents
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        // bodyType is the real data type that api returns
        val bodyType = getParameterUpperBound(0, observableType)
        // TODO: 2/25/22 既然这样，我们还要 adapter 的范型参数干啥
        return LiveDataCallAdapter<Any>(bodyType)
    }
}
