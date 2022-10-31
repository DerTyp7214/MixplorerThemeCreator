package de.dertyp7214.mixplorerthemecreator.core

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
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

@ColorInt
fun Int.changeHue(@IntRange(from = -360, to = 360) rotation: Int): Int {
    val hsl = floatArrayOf(0f, 0f, 0f)

    ColorUtils.colorToHSL(this, hsl)
    hsl[0] = if (hsl[0] + rotation > 360) hsl[0] + rotation - 360
    else if (hsl[0] + rotation < 0) hsl[0] + rotation + 360
    else hsl[0] + rotation

    return ColorUtils.HSLToColor(hsl)
}