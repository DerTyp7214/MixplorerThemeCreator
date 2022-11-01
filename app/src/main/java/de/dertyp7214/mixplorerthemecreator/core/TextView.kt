package de.dertyp7214.mixplorerthemecreator.core

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.widget.TextView
import androidx.annotation.ColorInt

fun TextView.setTextColor(@ColorInt color: Int, animated: Boolean = false) {
    if (animated) {
        ObjectAnimator.ofObject(
            this, "textColor",
            ArgbEvaluator(), textColors.defaultColor, color
        ).apply {
            duration =
                context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        }.start()
    } else setTextColor(color)
}