package de.dertyp7214.mixplorerthemecreator

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import de.dertyp7214.mixplorerthemecreator.components.IconPreviewAdapter
import de.dertyp7214.mixplorerthemecreator.components.SectionsPagerAdapter
import de.dertyp7214.mixplorerthemecreator.core.capitalize
import de.dertyp7214.mixplorerthemecreator.core.changeSaturation
import de.dertyp7214.mixplorerthemecreator.core.getAttr
import de.dertyp7214.mixplorerthemecreator.core.setBackground
import de.dertyp7214.mixplorerthemecreator.core.setCardBackgroundColor
import de.dertyp7214.mixplorerthemecreator.core.setIconTint
import de.dertyp7214.mixplorerthemecreator.core.setTextColor
import de.dertyp7214.mixplorerthemecreator.utils.ColorHelper
import de.dertyp7214.mixplorerthemecreator.utils.FileUtils
import de.dertyp7214.mixplorerthemecreator.utils.ThemeUtils
import java.io.File
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val previewCard by lazy { findViewById<CardView>(R.id.previewCard) }
    private val viewPager by lazy { findViewById<ViewPager>(R.id.viewPager) }
    private val dots by lazy { findViewById<ViewGroup>(R.id.dots) }

    private val editTextName by lazy { findViewById<EditText>(R.id.editTextName) }

    private val cardDark by lazy { findViewById<CardView>(R.id.cardDark) }
    private val cardLight by lazy { findViewById<CardView>(R.id.cardLight) }

    private val switchMonet by lazy { findViewById<SwitchCompat>(R.id.switchMonet) }
    private val switchTertiary by lazy { findViewById<SwitchCompat>(R.id.switchTertiary) }

    private val btnShare by lazy { findViewById<Button>(R.id.btnShare) }
    private val btnGenerateTheme by lazy { findViewById<Button>(R.id.btnGenerateTheme) }

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

    private val fileUtils = FileUtils(this)

    private val colorChangeListeners = arrayListOf({
        previewCard.setCardBackgroundColor(colorHelper.getColor("bg_page"), true)

        editTextName.setText(themeUtils.get("title"))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = adapter

        editTextName.doAfterTextChanged {
            it?.toString()?.let { title -> themeUtils.set("title", title) }
        }

        editTextName.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    editTextName.clearFocus()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                        editTextName.windowInsetsController?.hide(WindowInsets.Type.ime())
                    true
                }

                else -> false
            }
        }

        cardDark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        cardLight.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        switchMonet.setOnCheckedChangeListener { _, _ ->
            setColor()
        }

        switchTertiary.setOnCheckedChangeListener { _, _ ->
            setColor()
        }

        btnShare.setOnClickListener {
            createTheme()?.let(fileUtils::shareTheme)
        }

        btnGenerateTheme.setOnClickListener {
            createTheme()?.let(fileUtils::exportTheme)
        }

        setColor()
    }

    private fun createTheme(): File? {
        themeUtils.clean()

        themeUtils.exportXml()
        themeUtils.exportFont()
        themeUtils.exportIcons()

        return themeUtils.packTheme(themeUtils.get("title"))
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

            groupA.forEach { it.setTextColor(syntaxString, true) }
            groupB.forEach { it.setTextColor(syntaxKeyword, true) }
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
                    it.setTextColor(buttonTextColor, true)
                    it.setBackground(
                        ColorUtils.setAlphaComponent(
                            buttonTextColor,
                            (255 * .2f).roundToInt()
                        ), true
                    )
                } else it.setIconTint(buttonTextColor, true)
            }
        }

        colorChangeListeners.add(::changeColors)

        changeColors()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val iconsPreview: View.() -> Unit = {
        val icons = ArrayList<Pair<String, Bitmap>>()
        val adapter = IconPreviewAdapter(this@MainActivity, colorHelper, icons)

        themeUtils.exportIcons().listFiles()?.forEach { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val name = file.nameWithoutExtension.split("_").joinToString(" ") { it.capitalize() }

            icons.add(Pair(name, bitmap))
        }

        adapter.notifyDataSetChanged()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = adapter

        fun changeColors() {
            adapter.changeColors()
        }

        colorChangeListeners.add(::changeColors)

        changeColors()
    }

    private fun setColor() {
        if (switchMonet.isChecked) {
            colorHelper.setBackgroundColor(getAttr(com.google.android.material.R.attr.colorSurface))
            colorHelper.setBackgroundColorVariant(getAttr(com.google.android.material.R.attr.colorSurfaceVariant))
            colorHelper.setControlColor(
                if (switchTertiary.isChecked) getAttr(com.google.android.material.R.attr.colorTertiaryContainer)
                    .changeSaturation(.7f)
                else getAttr(com.google.android.material.R.attr.colorPrimaryContainer)
                    .changeSaturation(.7f)
            )
            colorHelper.setSyntaxColor(
                getAttr(com.google.android.material.R.attr.colorPrimary).changeSaturation(1.5f),
                getAttr(com.google.android.material.R.attr.colorTertiary).changeSaturation(2f)
            )
            colorHelper.setTextColorMain(getAttr(com.google.android.material.R.attr.colorOnSurface))
            colorHelper.setAccentColor(
                if (switchTertiary.isChecked) getAttr(com.google.android.material.R.attr.colorTertiary)
                else getAttr(com.google.android.material.R.attr.colorPrimary)
            )
            colorHelper.setTextColorMain(getAttr(com.google.android.material.R.attr.colorOnSurface))
            colorHelper.setTextColorSecondary(getAttr(com.google.android.material.R.attr.colorOnSecondaryContainer))
        } else colorHelper.resetColors()

        themeUtils.exportIcons()

        colorChangeListeners.forEach { it() }
    }
}