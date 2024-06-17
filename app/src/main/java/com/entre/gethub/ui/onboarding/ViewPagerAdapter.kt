package com.entre.gethub.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.entre.gethub.R

class ViewPagerAdapter(var context: Context) : PagerAdapter() {
    var images = intArrayOf(
        R.drawable.onboarding1,
        R.drawable.onboarding2,
        R.drawable.onboarding3,
        R.drawable.onboarding4
    )
    var headings = intArrayOf(
        R.string.onboarding_heading_one,
        R.string.onboarding_heading_two,
        R.string.onboarding_heading_three,
        R.string.onboarding_heading_four
    )

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.item_slider_layout, container, false)
        val slidetitleimage = view.findViewById<View>(R.id.ivImage) as ImageView
        val slideHeading = view.findViewById<View>(R.id.tvtitle) as TextView

        slidetitleimage.setImageResource(images[position])
        slideHeading.setText(headings[position])

        // Set the text size based on the position
        when (headings[position]) {
            R.string.onboarding_heading_one, R.string.onboarding_heading_two -> {
                slideHeading.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 25f)
            }
            R.string.onboarding_heading_three, R.string.onboarding_heading_four -> {
                slideHeading.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 20f)
            }
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
