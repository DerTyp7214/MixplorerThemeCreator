package de.dertyp7214.mixplorerthemecreator.core

import android.annotation.SuppressLint
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

val Context.isLightTheme
    get() = getAttr(com.google.android.material.R.attr.isLightTheme) != 0

fun Context.getAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

@SuppressLint("DiscouragedApi")
fun Context.getResourceId(variableName: String?, resourceName: String?, packageName: String? = getPackageName()): Int {
    return try {
        resources.getIdentifier(variableName, resourceName, packageName)
    } catch (_: Exception) {
        -1
    }
}

fun delayed(delay: Long, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(delay) { callback() }
}