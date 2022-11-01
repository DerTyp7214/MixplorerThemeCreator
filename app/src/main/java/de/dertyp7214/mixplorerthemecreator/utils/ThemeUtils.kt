package de.dertyp7214.mixplorerthemecreator.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentActivity
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.components.XMLFile
import de.dertyp7214.mixplorerthemecreator.core.getResourceId
import de.dertyp7214.mixplorerthemecreator.core.writeBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Suppress("unused")
class ThemeUtils(private val activity: FragmentActivity) {
    internal val context: Context = activity

    private val themePath = File(context.filesDir, "theme")

    internal var properties =
        XMLFile(activity.resources.openRawResource(R.raw.properties).bufferedReader()
            .use { it.readText() })
        private set

    fun getProperties() = properties.simpleMap()

    fun get(name: String, default: String = "") = properties.getValue(name)?.value ?: default

    fun set(name: String, value: Any) = properties.getValue(name)?.setValue(value)

    fun reset() {
        properties = XMLFile(activity.resources.openRawResource(R.raw.properties).bufferedReader()
            .use { it.readText() })
    }

    fun clean() {
        themePath.deleteRecursively()
    }

    fun packTheme(name: String = "theme"): File? {
        if (!themePath.exists()) return null

        val zipFile = File(context.filesDir, "${name.ifEmpty { "theme" }}.mit")

        ZipHelper.zip(listOf(themePath), zipFile)

        return if (zipFile.exists()) zipFile else null
    }

    fun exportXml() {
        if (!themePath.exists()) themePath.mkdirs()
        properties.writeFile(File(themePath, "properties.xml").absolutePath)
    }

    fun exportFont() {
        val fontPath = File(themePath, "fonts")
        if (!fontPath.exists()) fontPath.mkdirs()
        context.resources.openRawResource(R.raw.roboto_mono_regular).use { input ->
            FileOutputStream(File(fontPath, "roboto_mono_regular.ttf")).use { output ->
                input.copyTo(output)
            }
        }
    }

    fun exportIcons(colorHelper: ColorHelper, iconPack: IconPack = IconPack.MONO): File {
        val iconPath = File(themePath, "drawable")
        iconPath.deleteRecursively()
        iconPath.mkdirs()

        val zipFile = File(iconPath, "${iconPack.rawName}.zip")
        context.resources.openRawResource(context.getResourceId(iconPack.rawName, "raw"))
            .use { input ->
                FileOutputStream(zipFile).use { output ->
                    input.copyTo(output)
                }
            }

        ZipHelper.unpackZip(iconPath, zipFile)
        zipFile.delete()

        val baseColor = colorHelper.getColor("tint_bar_main_icons")
        val accentColor = colorHelper.getColor("tint_folder")

        iconPath.listFiles { file -> file.extension == "png" }?.forEach { file ->
            file.writeBitmap(
                tintDualColorImage(file.inputStream(), baseColor, accentColor),
                Bitmap.CompressFormat.PNG,
                100
            )
        }

        return iconPath
    }

    fun iconPackPreview(colorHelper: ColorHelper, iconPack: IconPack): Bitmap {
        val baseColor = colorHelper.getColor("tint_bar_main_icons")
        val accentColor = colorHelper.getColor("tint_folder")

        return context.resources
            .openRawResource(context.getResourceId("${iconPack.rawName}_preview", "raw"))
            .use { input ->
                tintDualColorImage(input, baseColor, accentColor)
            }
    }

    private fun tintDualColorImage(
        imageStream: InputStream,
        @ColorInt baseColor: Int,
        @ColorInt accentColor: Int
    ): Bitmap {
        val bitmap = BitmapFactory.decodeStream(imageStream).copy(Bitmap.Config.ARGB_8888, true)

        val width = bitmap.width
        val height = bitmap.height

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        pixels.forEachIndexed { index, pixel ->
            val alpha = Color.alpha(pixel)
            val maxAlphaPixel = ColorUtils.setAlphaComponent(pixel, 255)
            pixels[index] = when (maxAlphaPixel) {
                Color.RED -> ColorUtils.setAlphaComponent(baseColor, alpha)
                Color.BLUE -> ColorUtils.setAlphaComponent(accentColor, alpha)
                else -> pixel
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

        return bitmap
    }

    enum class IconPack(val rawName: String) {
        DUAL("mixdual"),
        MONO("mixmono");
    }
}