package me.ikvarxt.halo.extentions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.ikvarxt.halo.R
import me.ikvarxt.halo.application

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    fun show() = Toast.makeText(this, msg, duration).show()

    if (Looper.getMainLooper() == Looper.myLooper()) {
        show()
    } else {
        Handler(Looper.getMainLooper()).post { show() }
    }
}

fun Fragment.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    val context = requireContext()
    fun show() = Toast.makeText(context, msg, duration).show()

    if (Looper.getMainLooper() == Looper.myLooper()) {
        show()
    } else {
        Handler(Looper.getMainLooper()).post { show() }
    }
}

fun showErrorToast(msg: String? = null, duration: Int = Toast.LENGTH_LONG) {
    val context = application
    val defaultError = context.getString(R.string.some_error)
    val message = msg ?: defaultError

    fun show() = Toast.makeText(context, message, duration).show()

    if (Looper.getMainLooper() == Looper.myLooper()) {
        show()
    } else {
        Handler(Looper.getMainLooper()).post { show() }
    }
}

fun showNetworkErrorToast(msg: String? = null) {
    val context = application
    val default = context.getString(R.string.network_error)
    showErrorToast(msg ?: default)
}