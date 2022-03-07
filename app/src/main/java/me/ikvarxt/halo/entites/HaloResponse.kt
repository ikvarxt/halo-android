package me.ikvarxt.halo.entites

data  class HaloResponse<T>(
    val status: Int,
    val message: String,
    val data: T
)