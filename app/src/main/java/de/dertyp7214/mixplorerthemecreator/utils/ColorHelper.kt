package de.dertyp7214.mixplorerthemecreator.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.view.ContextThemeWrapper
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.components.XMLEntry
import de.dertyp7214.mixplorerthemecreator.components.XMLFile
import de.dertyp7214.mixplorerthemecreator.core.changeHue
import de.dertyp7214.mixplorerthemecreator.core.changeSaturation
import de.dertyp7214.mixplorerthemecreator.core.getAttr

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ColorHelper(
    private val themeUtils: ThemeUtils
) {
    private val context = themeUtils.context

    private val colorGroupBackground = listOf(
        "tint_status_bar",
        "tint_navigation_bar",
        "bg_bar_action",
        "bg_bar_main",
        "dialog_footer_right",
        "bg_bar_tools",
        "bg_page",
        "bg_bar_tab",
        "text_popup_primary",
        "text_popup_secondary",
        "tint_divider_files",
        "tint_divider_popup_list",
        "tint_divider_settings",
        "tint_popup_bg",
        "tint_widget_off",
        "tint_widget_on",
        "splash_screen"
    )

    private val colorGroupTextMain = listOf(
        "text_bar_main_primary",
        "highlight_bar_action_buttons",
        "highlight_bar_main_buttons",
        "highlight_bar_tab_buttons",
        "highlight_bar_tool_buttons",
        "text_link_pressed",
        "text_editor_foreground",
        "text_edit_box",
        "text_edit_box_hint",
        "text_edit_selection_foreground",
        "text_filter_box",
        "text_grid_primary",
        "text_grid_primary_inverse",
        "text_grid_secondary_inverse",
        "text_popup_header",
        "text_popup_primary",
        "text_popup_primary_inverse",
        "text_popup_secondary_inverse",
        "tint_bar_action_icons",
        "tint_bar_main_icons",
        "tint_bar_tab_icons",
        "tint_bar_tools_icons",
        "tint_edge_effect"
    )

    private val colorGroupTextSecondary = listOf(
        "text_bar_main_secondary",
        "text_edit_selection_background",
        "text_filter_box_hint",
        "text_grid_secondary",
        "text_popup_secondary",
        "text_scroll_overlay",
        "tint_page_separator"
    )

    private val colorGroupBackgroundVariant = listOf(
        "text_bar_tab_default",
        "highlight_grid_item",
        "highlight_popup_list_item",
        "text_popup_primary_inverse",
        "text_popup_secondary_inverse",
        "tint_grid_item"
    )

    private val colorGroupControl = listOf(
        "tint_tab_indicator",
        "tint_notification_buttons",
        "highlight_popup_controls",
        "tint_popup_controls",
        "tint_progress_track"
    )

    private val colorGroupAccent = listOf(
        "tint_tab_indicator_selected",
        "text_bar_tab_selected",
        "text_link",
        "text_button",
        "text_button_inverse",
        "tint_scroll_thumbs",
        "tint_folder",
        "tint_popup_controls_pressed",
        "tint_popup_icons",
        "tint_progress_bar",
        "tint_icon_signs"
    )

    private val syntaxAttr = "syntax_attr"
    private val syntaxAttrValue = "syntax_attr_value"
    private val syntaxComment = "syntax_comment"
    private val syntaxKeyword = "syntax_keyword"
    private val syntaxString = "syntax_string"
    private val syntaxSymbol = "syntax_symbol"

    fun setBackgroundColor(@ColorInt color: Int) =
        colorGroupBackground.forEach { setColor(it, color) }

    fun setTextColorMain(@ColorInt color: Int) =
        colorGroupTextMain.forEach { setColor(it, color) }

    fun setTextColorSecondary(@ColorInt color: Int) =
        colorGroupTextSecondary.forEach { setColor(it, color) }

    fun setBackgroundColorVariant(@ColorInt color: Int) =
        colorGroupBackgroundVariant.forEach { setColor(it, color) }

    fun setControlColor(@ColorInt color: Int) =
        colorGroupControl.forEach { setColor(it, color) }

    fun setAccentColor(@ColorInt color: Int) =
        colorGroupAccent.forEach { setColor(it, color) }

    fun setSyntaxColor(@ColorInt primary: Int, @ColorInt tertiary: Int) {
        setSyntaxAttrColor(primary.changeHue(40))
        setSyntaxAttrValueColor(tertiary)
        setSyntaxCommentColor(primary.changeSaturation(.1f))
        setSyntaxKeywordColor(primary)
        setSyntaxStringColor(tertiary)
        setSyntaxSymbolColor(primary.changeHue(-60).changeSaturation(.6f))
    }

    fun setSyntaxAttrColor(@ColorInt color: Int) = setColor(syntaxAttr, color)
    fun setSyntaxAttrValueColor(@ColorInt color: Int) = setColor(syntaxAttrValue, color)
    fun setSyntaxCommentColor(@ColorInt color: Int) = setColor(syntaxComment, color)
    fun setSyntaxKeywordColor(@ColorInt color: Int) = setColor(syntaxKeyword, color)
    fun setSyntaxStringColor(@ColorInt color: Int) = setColor(syntaxString, color)
    fun setSyntaxSymbolColor(@ColorInt color: Int) = setColor(syntaxSymbol, color)

    fun resetColors() {
        val tmp = XMLFile(
            context.resources
                .openRawResource(R.raw.properties)
                .bufferedReader().use { it.readText() }
        )

        val keys = ArrayList<String>()
        keys.addAll(colorGroupBackground)
        keys.addAll(colorGroupAccent)
        keys.addAll(colorGroupBackgroundVariant)
        keys.addAll(colorGroupControl)
        keys.addAll(colorGroupTextMain)
        keys.addAll(colorGroupTextSecondary)

        keys.add(syntaxAttr)
        keys.add(syntaxAttrValue)
        keys.add(syntaxComment)
        keys.add(syntaxKeyword)
        keys.add(syntaxString)
        keys.add(syntaxSymbol)

        keys.forEach { key ->
            tmp.getValue(key)?.value?.let { themeUtils.properties.getValue(key)?.setValue(it) }
        }
    }

    var dark: Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) context.resources.configuration.isNightModeActive
        else context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        set(value) {
            field = value
            contextWrapper = createNightModeContext(context, value)
        }

    private var contextWrapper = createNightModeContext(context, dark)

    fun getAttr(@AttrRes attr: Int) = contextWrapper.getAttr(attr)
    fun getColor(@ColorRes color: Int) = contextWrapper.getColor(color)

    fun getColor(name: String, default: Int = Color.RED) =
        themeUtils.properties.getValue(name)?.getColor() ?: default

    fun setColor(name: String, @ColorInt color: Int) =
        themeUtils.properties.getValue(name)?.setValue(
            String.format(
                "#%06X",
                0xFFFFFF and color
            )
        ) ?: themeUtils.properties.setValue(
            XMLEntry(
                name, String.format(
                    "#%06X",
                    0xFFFFFF and color
                )
            )
        )

    private fun createNightModeContext(context: Context, isNightMode: Boolean): Context {
        val uiModeFlag =
            if (isNightMode) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO

        return ContextThemeWrapper(context, context.theme).apply {
            val config = Configuration(context.resources.configuration)
            config.uiMode = uiModeFlag or (config.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
            applyOverrideConfiguration(config)
        }
    }
}