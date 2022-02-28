package me.ikvarxt.halo.network.infra

import me.ikvarxt.halo.network.infra.Status.SUCCESS
import me.ikvarxt.halo.network.infra.Status.ERROR
import me.ikvarxt.halo.network.infra.Status.LOADING

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
// TODO: 2/25/22 其实这个类没有使用密封类，为什么呢，肯定有不合理的考虑，思考下
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}
