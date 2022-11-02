package de.dertyp7214.mixplorerthemecreator.core

import android.graphics.Color

fun isColor(string: String): Boolean = try {
    Color.parseColor(string)
    true
} catch (_: Exception) {
    false
}