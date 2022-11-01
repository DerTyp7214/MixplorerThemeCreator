package de.dertyp7214.mixplorerthemecreator.core

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.core.os.postDelayed

val Context.colorSurface
    get() = getAttr(com.google.android.material.R.attr.colorSurface)
val Context.colorPrimary
    get() = getAttr(com.google.android.material.R.attr.colorPrimary)

fun Context.getAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun delayed(delay: Long, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(delay) { callback() }
}