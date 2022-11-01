package de.dertyp7214.mixplorerthemecreator.core

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView

fun CardView.setCardBackgroundColor(@ColorInt color: Int, animated: Boolean = false) {
    if (animated) {
        ObjectAnimator.ofObject(
            this, "cardBackgroundColor",
            ArgbEvaluator(), cardBackgroundColor.defaultColor, color
        ).apply {
            duration =
                context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        }.start()
    } else setCardBackgroundColor(color)
}