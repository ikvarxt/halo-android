package me.ikvarxt.halo.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "halo_options")
data class HaloOption(
    @PrimaryKey
    val key: String,
    val value: String
)

enum class HaloOptionType {
    INTERNAL, CUSTOM
}