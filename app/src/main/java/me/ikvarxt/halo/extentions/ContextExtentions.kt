package me.ikvarxt.halo.extentions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    fun show() = Toast.makeText(this, msg, duration).show()

    if (Looper.getMainLooper() == Looper.myLooper()) {
        show()
    } else {
        Handler(Looper.getMainLooper()).post { show() }
    }
}