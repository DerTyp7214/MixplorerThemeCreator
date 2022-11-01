package de.dertyp7214.mixplorerthemecreator.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.core.capitalize
import de.dertyp7214.mixplorerthemecreator.utils.ColorHelper
import de.dertyp7214.mixplorerthemecreator.utils.ThemeUtils
import de.dertyp7214.mixplorerthemecreator.utils.doIoInBackground
import de.dertyp7214.mixplorerthemecreator.utils.runOnMain

class IconPackBottomSheet(
    private val colorHelper: ColorHelper,
    private val onSelect: (ThemeUtils.IconPack) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var adapter: Adapter

    private val list = ArrayList<Pair<ThemeUtils.IconPack, Bitmap>>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.icon_pack_bottomsheet, container, false)

        list.clear()

        adapter = Adapter(requireContext(), list) {
            dismiss()
            onSelect(it)
        }

        doIoInBackground {
            ThemeUtils.IconPack.values().forEach { pack ->
                list.add(Pair(pack, colorHelper.iconPackPreview(pack)))
            }
            runOnMain {
                adapter.notifyDataSetChanged()
            }
        }

        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerViewIconPacks)

        recyclerView.adapter = adapter

        return v
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, "icon_pack_bottom_sheet")
    }

    class Adapter(
        private val context: Context,
        private val list: List<Pair<ThemeUtils.IconPack, Bitmap>>,
        private val onSelect: (ThemeUtils.IconPack) -> Unit
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val wrapper: ViewGroup = v.findViewById(R.id.wrapper)

            val icon: ImageView = v.findViewById(R.id.icon)
            val title: TextView = v.findViewById(R.id.title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.icon_pack_item, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val (iconPack, image) = list[position]

            holder.title.text = iconPack.rawName.capitalize()
            holder.icon.setImageBitmap(image)

            holder.wrapper.setOnClickListener {
                onSelect(iconPack)
            }
        }

        override fun getItemCount() = list.size
    }
}