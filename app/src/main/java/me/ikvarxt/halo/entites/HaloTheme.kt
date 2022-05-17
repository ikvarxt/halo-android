package me.ikvarxt.halo.entites

import com.google.gson.annotations.SerializedName
import me.ikvarxt.halo.di.NetworkModule

data class HaloTheme(
    val id: String,
    val name: String,
    val activated: Boolean,
    @SerializedName("logo") val authorAvatar: String,
    private val screenshots: String,
) {
    val screenshotsPath: String
        get() = "${NetworkModule.PLACEHOLDER_DOMAIN}$screenshots"
}