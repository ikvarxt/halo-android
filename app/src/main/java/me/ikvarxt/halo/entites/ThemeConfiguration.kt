package me.ikvarxt.halo.entites

import com.google.gson.annotations.SerializedName

data class ThemeConfigurationGroup(
    @SerializedName("name")
    val id: String,
    val label: String,
    val items: List<ThemeConfiguration>
)

data class ThemeConfiguration(
    @SerializedName("name")
    val id: String,
    val label: String,
    val type: ThemeConfigType,
    val dataType: ThemeConfigDataType,
    val defaultValue: String,
    val placeholder: String,
    val description: String,
    val options: List<ThemeConfigOption>?
)

enum class ThemeConfigType {
    COLOR,
    RADIO,
    ATTACHMENT,
    TEXT,
}

enum class ThemeConfigDataType {
    STRING,
    BOOL,
}

data class ThemeConfigOption(
    val label: String,
    val value: Boolean,
)