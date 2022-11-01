package de.dertyp7214.mixplorerthemecreator.core

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.view.ViewGroup
import androidx.annotation.ColorInt

fun ViewGroup.setRippleColor(
    @ColorInt rippleColor: Int,
    @ColorInt backgroundColor: Int
) {
    fun rippleDrawable(@ColorInt rippleColor: Int, @ColorInt backgroundColor: Int) = RippleDrawable(
        ColorStateList(
            arrayOf(intArrayOf()),
            intArrayOf(rippleColor)
        ),
        ColorDrawable(backgroundColor),
        null
    )
    background = rippleDrawable(rippleColor, backgroundColor)
}