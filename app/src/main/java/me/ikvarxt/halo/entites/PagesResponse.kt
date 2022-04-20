package me.ikvarxt.halo.entites

data class PagesResponse<T>(
    val content: List<T>?,
    val hasContent: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val isEmpty: Boolean,
    val isFirst: Boolean,
    val isLast: Boolean,
    val page: Int,
    val pages: Int,
    val rpp: Int,
    val total: Int,
)
