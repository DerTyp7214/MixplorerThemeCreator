package de.dertyp7214.mixplorerthemecreator.components

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.madrapps.pikolo.HSLColorPicker
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.core.openDialog
import de.dertyp7214.mixplorerthemecreator.utils.ColorHelper
import java.util.Locale
import java.util.regex.Pattern

class ColorPickerBottomSheet(
    private val colorHelper: ColorHelper,
    private val advanced: Boolean = false,
    private val colorChanged: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var adapter: ColorAdapter

    private val list = ArrayList<ColorEntry>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.color_picker_bottomsheet, container, false)

        adapter = ColorAdapter(requireContext(), list) { key ->
            val color = if (advanced) colorHelper.getColor(key)
            else colorHelper.getGroupColor(key)

            requireActivity().openDialog(
                R.layout.color_picker_dialog,
                cancelable = false
            ) { dialog ->
                val colorPicker: HSLColorPicker = findViewById(R.id.colorPicker)
                val editText: EditText = findViewById(R.id.editText)
                val btnDice: Button = findViewById(R.id.btnDice)
                val btnCancel: Button = findViewById(R.id.btnCancel)
                val btnPick: Button = findViewById(R.id.btnPick)

                editText.filters = arrayOf(
                    InputFilter { source, _, end, _, _, _ ->
                        val pattern = Pattern.compile("^\\p{XDigit}+$")

                        val stringBuilder = StringBuilder("")

                        for (i in 0 until end) {
                            if (!Character.isLetterOrDigit(source[i]) && !Character.isSpaceChar(
                                    source[i]
                                )
                            ) continue

                            val matcher = pattern.matcher(String(charArrayOf(source[i])))
                            if (!matcher.matches()) continue
                            if (i >= 6) continue

                            stringBuilder.append(source[i])
                        }

                        return@InputFilter stringBuilder.toString().uppercase(Locale.getDefault())
                    }
                )

                editText.setText(Integer.toHexString(color).substring(2))

                colorPicker.setColor(color)
                colorPicker.setColorSelectionListener(object : SimpleColorSelectionListener() {
                    override fun onColorSelected(color: Int) {
                        editText.setText(Integer.toHexString(color).substring(2))
                    }
                })

                editText.doAfterTextChanged {
                    val text = it?.toString()
                    btnPick.isEnabled = true
                    if (text != null && text.length == 6)
                        colorPicker.setColor(parseColor(text))
                    else btnPick.isEnabled = false
                }

                btnDice.setOnClickListener {
                    val randomColor = randomColor()
                    editText.setText(randomColor)
                    colorPicker.setColor(parseColor(randomColor))
                }
                btnCancel.setOnClickListener { dialog.dismiss() }
                btnPick.setOnClickListener {
                    dialog.dismiss()
                    if (advanced) colorHelper.setColor(key, parseColor(editText.text.toString()))
                    else colorHelper.setGroupColor(key, parseColor(editText.text.toString()))
                }
            }.setOnDismissListener {
                filteredList()
                colorChanged()
            }
        }

        filteredList()

        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerViewColorPicker)

        recyclerView.adapter = adapter

        return v
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filteredList(filter: String = "", advanced: Boolean = this.advanced) {
        list.clear()
        if (advanced) list.addAll(colorHelper.getColors(filter)
            .entries.sortedBy { it.getColor() }
            .map { ColorEntry(it.key, it.getColor()) })
        else list.addAll(colorHelper.getColorGroups())

        adapter.notifyDataSetChanged()
    }

    private fun parseColor(string: String): Int {
        return try {
            Color.parseColor(string.let {
                if (it.startsWith("#")) it else "#$it"
            })
        } catch (_: Exception) {
            Color.RED
        }
    }

    private fun randomColor(): String = List(6) {
        (('A'..'F') + ('0'..'9')).random()
    }.joinToString("")

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, "color_picker_bottom_sheet")
    }

    data class ColorEntry(
        val key: String,
        @ColorInt val color: Int
    )
}