package me.ikvarxt.halo.extentions

fun String.toSlug(separator: String = "-"): String {
    val hanziToPinyin = HanziToPinyin.getInstance()
    val tokens = hanziToPinyin.get(this)
    return tokens.joinToString(separator) { it.target.lowercase() }
}