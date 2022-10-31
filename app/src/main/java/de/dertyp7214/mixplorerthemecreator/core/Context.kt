package de.dertyp7214.mixplorerthemecreator.core

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}