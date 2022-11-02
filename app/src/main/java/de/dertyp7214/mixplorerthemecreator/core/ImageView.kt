package de.dertyp7214.mixplorerthemecreator.core

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toDrawable

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

fun ImageView.setImage(bitmap: Bitmap, animated: Boolean = false) {
    val image = drawable
    if (animated && image != null) {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration =
                context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            addUpdateListener {
                setImageDrawable(
                    image.blend(
                        bitmap.toDrawable(resources),
                        it.animatedValue as Float
                    )
                )
            }
        }.start()
    } else setImageBitmap(bitmap)
}