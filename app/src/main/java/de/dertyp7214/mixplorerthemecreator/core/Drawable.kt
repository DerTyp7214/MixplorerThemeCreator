package de.dertyp7214.mixplorerthemecreator.core

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import kotlin.math.roundToInt

fun Drawable.blend(drawable: Drawable, factor: Float): Drawable {
    val bg = copy()
    val fg = drawable.copy()

    if (bg == null || fg == null) return this

    val baseAlpha = (255 * factor).roundToInt()

    bg.alpha = 255 - baseAlpha
    fg.alpha = baseAlpha

    return LayerDrawable(arrayOf(bg, fg))
}

fun Drawable.copy() = constantState?.newDrawable()?.mutate()