package de.dertyp7214.mixplorerthemecreator.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.core.capitalize
import de.dertyp7214.mixplorerthemecreator.core.setImageTint

class ColorAdapter(
    private val context: Context,
    private val list: List<ColorPickerBottomSheet.ColorEntry>,
    private val onClick: (key: String) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val wrapper: ViewGroup = v.findViewById(R.id.wrapper)
        val title: TextView = v.findViewById(R.id.title)
        val subTitle: TextView = v.findViewById(R.id.subTitle)
        val colorPreview: ImageView = v.findViewById(R.id.colorPreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.color_preview, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorEntry = list[position]

        holder.title.text = colorEntry.key.split("_").joinToString(" ") { it.capitalize() }
        holder.subTitle.text = colorEntry.key
        holder.colorPreview.setImageTint(colorEntry.color)

        holder.wrapper.setOnClickListener {
            onClick(colorEntry.key)
        }
    }

    override fun getItemCount() = list.size
}