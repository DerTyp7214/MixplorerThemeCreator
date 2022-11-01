package de.dertyp7214.mixplorerthemecreator.core

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt

fun ImageView.setImageTint(@ColorInt color: Int, animated: Boolean = false) {
    val defaultColor = imageTintList?.defaultColor
    if (animated && defaultColor != null) {
        ValueAnimator.ofArgb(
            defaultColor, color
        ).apply {
            duration =
                context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            addUpdateListener {
                imageTintList = ColorStateList.valueOf(it.animatedValue as Int)
            }
        }.start()
    } else imageTintList = ColorStateList.valueOf(color)
}