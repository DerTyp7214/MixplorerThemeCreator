package de.dertyp7214.mixplorerthemecreator.core

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.widget.Button
import androidx.annotation.ColorInt
import com.google.android.material.button.MaterialButton

fun Button.setBackground(@ColorInt color: Int, animated: Boolean = false) {
    val defaultColor = backgroundTintList?.defaultColor
    if (animated && defaultColor != null) {
        ValueAnimator.ofArgb(
            defaultColor, color
        ).apply {
            duration =
                context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            addUpdateListener {
                backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
            }
        }.start()
    } else backgroundTintList = ColorStateList.valueOf(color)
}

fun MaterialButton.setIconTint(@ColorInt color: Int, animated: Boolean = false) {
    val defaultColor = iconTint?.defaultColor
    if (animated && defaultColor != null) {
        ValueAnimator.ofArgb(
            defaultColor, color
        ).apply {
            duration =
                context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            addUpdateListener {
                iconTint = ColorStateList.valueOf(it.animatedValue as Int)
            }
        }.start()
    } else iconTint = ColorStateList.valueOf(color)
}