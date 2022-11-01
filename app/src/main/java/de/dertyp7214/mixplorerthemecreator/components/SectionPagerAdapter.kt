package de.dertyp7214.mixplorerthemecreator.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.graphics.ColorUtils
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.core.view.updatePadding
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.core.colorPrimary
import de.dertyp7214.mixplorerthemecreator.core.colorSurface
import de.dertyp7214.mixplorerthemecreator.core.dpToPxRounded
import de.dertyp7214.mixplorerthemecreator.core.setImageTint

class SectionsPagerAdapter(
    private val viewPager: ViewPager,
    private val dots: ViewGroup,
    private val pages: List<Page>
) : PagerAdapter() {

    private val context = viewPager.context

    init {
        for (i in 0 until count) {
            dots.addView(ImageView(context).apply {
                val size = 8.dpToPxRounded(context)
                val padding = 4.dpToPxRounded(context)

                layoutParams = ViewGroup.LayoutParams(size + (padding * 2), size)
                updatePadding(left = padding, right = padding)

                setImageResource(R.drawable.default_dot)

                if (i == 0) setImageTint(context.colorPrimary)
                else setImageTint(context.colorSurface)

                setOnClickListener {
                    viewPager.setCurrentItem(i, true)
                }
            })
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val colorPrimary = context.colorPrimary
                val colorSurface = context.colorSurface

                val currentDot = dots[position]
                val nextDot = dots.getChildAt(position + 1)

                if (currentDot is ImageView && nextDot is ImageView && positionOffset > 0) {
                    val currentColor =
                        ColorUtils.blendARGB(colorPrimary, colorSurface, positionOffset)
                    val nextColor =
                        ColorUtils.blendARGB(colorSurface, colorPrimary, positionOffset)

                    currentDot.setImageTint(currentColor)
                    nextDot.setImageTint(nextColor)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageSelected(position: Int) {
                dots.forEachIndexed { index, view ->
                    if (view is ImageView) {
                        if (index == position) view.setImageTint(context.colorPrimary)
                        else view.setImageTint(context.colorSurface)
                    }
                }
            }
        })
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val page = pages[position]

        val layout = LayoutInflater.from(context)
            .inflate(page.layoutId, container, false)
            .apply(page.block)

        container.addView(layout)

        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }

    override fun getCount() = pages.size
    override fun isViewFromObject(view: View, any: Any) = view == any

    data class Page(
        @LayoutRes val layoutId: Int,
        val block: View.() -> Unit
    )
}