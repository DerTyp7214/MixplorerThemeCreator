package de.dertyp7214.mixplorerthemecreator.core

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

fun Number.dpToPx(context: Context): Float {
    return this.toFloat() * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Number.dpToPxRounded(context: Context): Int {
    return dpToPx(context).roundToInt()
}

@ColorInt
fun Int.changeSaturation(factor: Float): Int {
    val hsl = floatArrayOf(0f, 0f, 0f)

    ColorUtils.colorToHSL(this, hsl)
    hsl[1] *= factor

    return ColorUtils.HSLToColor(hsl)
}