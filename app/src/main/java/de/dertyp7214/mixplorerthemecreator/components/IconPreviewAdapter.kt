package de.dertyp7214.mixplorerthemecreator.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.core.setImage
import de.dertyp7214.mixplorerthemecreator.core.setRippleColor
import de.dertyp7214.mixplorerthemecreator.core.setTextColor
import de.dertyp7214.mixplorerthemecreator.utils.ColorHelper

class IconPreviewAdapter(
    private val context: Context,
    private val colorHelper: ColorHelper,
    private val icons: List<Pair<String, Bitmap>>
) : RecyclerView.Adapter<IconPreviewAdapter.ViewHolder>() {

    private val colorChangeListeners = ArrayList<() -> Unit>()

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val wrapper: ViewGroup = v.findViewById(R.id.wrapper)
        val icon: ImageView = v.findViewById(R.id.icon)
        val text: TextView = v.findViewById(R.id.text)
        val textDate: TextView = v.findViewById(R.id.textDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.preview_recycler_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (iconName, icon) = icons[position]

        holder.text.text = iconName
        holder.icon.setImage(icon)

        fun changeColors() {
            val tintBarMainIcons = colorHelper.getColor("tint_bar_main_icons")
            val rippleColor = colorHelper.getColor("tint_grid_item")

            holder.text.setTextColor(tintBarMainIcons, true)
            holder.textDate.setTextColor(tintBarMainIcons, true)
            holder.wrapper.setRippleColor(rippleColor, Color.TRANSPARENT)
        }

        colorChangeListeners.add(::changeColors)

        changeColors()
    }

    fun changeColors() = colorChangeListeners.forEach { it() }

    override fun getItemCount() = icons.size
}