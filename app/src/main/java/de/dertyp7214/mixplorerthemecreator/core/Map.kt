package de.dertyp7214.mixplorerthemecreator.core

fun <K, V> Map<K, V>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((Map.Entry<K, V>) -> CharSequence)? = null
): String {
    return map { it }.joinToString(separator, prefix, postfix, limit, truncated, transform)
}