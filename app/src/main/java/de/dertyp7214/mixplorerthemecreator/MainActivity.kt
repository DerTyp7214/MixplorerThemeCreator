package de.dertyp7214.mixplorerthemecreator

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import de.dertyp7214.mixplorerthemecreator.components.SectionsPagerAdapter
import de.dertyp7214.mixplorerthemecreator.core.changeSaturation
import de.dertyp7214.mixplorerthemecreator.core.getAttr
import de.dertyp7214.mixplorerthemecreator.utils.ColorHelper
import de.dertyp7214.mixplorerthemecreator.utils.ThemeUtils
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val previewCard by lazy { findViewById<MaterialCardView>(R.id.previewCard) }
    private val viewPager by lazy { findViewById<ViewPager>(R.id.viewPager) }
    private val dots by lazy { findViewById<ViewGroup>(R.id.dots) }

    private val adapter by lazy { SectionsPagerAdapter(viewPager, dots, pages) }

    private val pages by lazy {
        listOf(
            SectionsPagerAdapter.Page(R.layout.preview_icons, iconsPreview),
            SectionsPagerAdapter.Page(R.layout.preview_html, htmlPreview),
            SectionsPagerAdapter.Page(R.layout.preview_dialog, dialogPreview)
        )
    }

    private val themeUtils by lazy { ThemeUtils(this) }
    private val colorHelper by lazy { ColorHelper(themeUtils) }

    private val colorChangeListeners = arrayListOf({
        previewCard.setCardBackgroundColor(colorHelper.getColor("bg_page"))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = adapter

        colorChangeListeners.forEach { it() }
        setColor()

        themeUtils.exportXml()
    }

    private val htmlPreview: View.() -> Unit = {
        val groupA: List<TextView> = listOf(
            findViewById(R.id.textDoctype),
            findViewById(R.id.textH1Content),
            findViewById(R.id.textPContent),
        )
        val groupB: List<TextView> = listOf(
            findViewById(R.id.textCodeOpen),
            findViewById(R.id.textH1Open),
            findViewById(R.id.textH1Close),
            findViewById(R.id.textPOpen),
            findViewById(R.id.textPClose),
            findViewById(R.id.textCodeClose),
        )

        fun changeColors() {
            val syntaxKeyword = colorHelper.getColor("syntax_keyword")
            val syntaxString = colorHelper.getColor("syntax_string")

            groupA.forEach { it.setTextColor(syntaxString) }
            groupB.forEach { it.setTextColor(syntaxKeyword) }
        }

        colorChangeListeners.add(::changeColors)

        changeColors()
    }

    private val dialogPreview: View.() -> Unit = {
        val groupA: List<TextView> = listOf(
            findViewById(R.id.textTitle),
            findViewById(R.id.textText1),
            findViewById(R.id.textText2),
        )
        val groupB: List<MaterialButton> = listOf(
            findViewById(R.id.btnClose),
            findViewById(R.id.button),
        )

        fun changeColors() {
            val textBarMainPrimary = colorHelper.getColor("text_bar_main_primary")
            val buttonTextColor = colorHelper.getColor("text_button")

            groupA.forEach { it.setTextColor(textBarMainPrimary) }
            groupB.forEach {
                if (it.icon == null) {
                    it.setTextColor(buttonTextColor)
                    it.backgroundTintList =
                        ColorStateList.valueOf(
                            ColorUtils.setAlphaComponent(
                                buttonTextColor,
                                (255 * .2f).roundToInt()
                            )
                        )
                } else it.iconTint = ColorStateList.valueOf(buttonTextColor)
            }
        }

        colorChangeListeners.add(::changeColors)

        changeColors()
    }

    private val iconsPreview: View.() -> Unit = {
        val groupA: List<TextView> = listOf(
            findViewById(R.id.textFolder),
            findViewById(R.id.textFolderDate),
            findViewById(R.id.textFile),
            findViewById(R.id.textFileDate),
            findViewById(R.id.textArchive),
            findViewById(R.id.textArchiveDate)
        )
        val groupB: List<ImageView> = listOf(
            findViewById(R.id.iconFolder),
            findViewById(R.id.iconFile),
            findViewById(R.id.iconArchive)
        )

        fun changeColors() {
            val tintBarMainIcons = colorHelper.getColor("tint_bar_main_icons")

            groupA.forEach { it.setTextColor(tintBarMainIcons) }
            groupB.forEach { it.imageTintList = ColorStateList.valueOf(tintBarMainIcons) }
        }

        colorChangeListeners.add(::changeColors)

        changeColors()
    }

    private fun setColor() {
        colorHelper.setBackgroundColor(getAttr(com.google.android.material.R.attr.colorSurface))
        colorHelper.setSyntaxKeywordColor(
            getAttr(com.google.android.material.R.attr.colorPrimary).changeSaturation(1.5f)
        )
        colorHelper.setSyntaxStringColor(
            getAttr(com.google.android.material.R.attr.colorTertiary).changeSaturation(2f)
        )
        colorHelper.setTextColorMain(getAttr(com.google.android.material.R.attr.colorOnSurface))
        colorHelper.setAccentColor(getAttr(com.google.android.material.R.attr.colorPrimary))
        colorHelper.setTextColorMain(getAttr(com.google.android.material.R.attr.colorOnSurface))
        colorHelper.setTextColorSecondary(getAttr(com.google.android.material.R.attr.colorOnSecondaryContainer))

        colorChangeListeners.forEach { it() }
    }
}