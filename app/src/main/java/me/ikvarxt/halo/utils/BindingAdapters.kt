package me.ikvarxt.halo.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.bindImageWithUrl(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}