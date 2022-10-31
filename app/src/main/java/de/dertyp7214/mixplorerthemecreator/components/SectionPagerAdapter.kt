package de.dertyp7214.mixplorerthemecreator.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.view.forEachIndexed
import androidx.core.view.updatePadding
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.core.dpToPxRounded

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

                setImageResource(R.drawable.tab_selector)

                if (i == 0) isSelected = true
            })
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageSelected(position: Int) {
                dots.forEachIndexed { index, view ->
                    view.isSelected = index == position
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