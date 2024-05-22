package com.bangkit.gethub.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bangkit.gethub.R
import com.bangkit.gethub.ui.auth.LoginActivity
import com.bangkit.gethub.ui.auth.RegisterActivity


class OnboardingActivity : AppCompatActivity() {
    private var mSLideViewPager: ViewPager? = null
    private var mDotLayout: LinearLayout? = null
    private var backbtn: Button? = null
    private var nextbtn: Button? = null
    private var skipbtn: Button? = null
    private lateinit var dots: Array<TextView>
    private var viewPagerAdapter: ViewPagerAdapter? = null

    private val descriptions = arrayOf(
        R.string.onboarding_desc_one,
        R.string.onboarding_desc_two
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Mendapatkan referensi ke tombol Masuk
        val masukButton: ImageButton = findViewById(R.id.button_masuk)

        // Menambahkan listener klik pada tombol Masuk
        masukButton.setOnClickListener {
            // Membuat Intent untuk AuthActivity
            val intent = Intent(this, LoginActivity::class.java)

            // Menjalankan intent
            startActivity(intent)
        }

        // Mendapatkan referensi ke tombol Masuk
        val daftarButton: ImageButton = findViewById(R.id.button_daftar)

        // Menambahkan listener klik pada tombol Masuk
        daftarButton.setOnClickListener {
            // Membuat Intent untuk AuthActivity
            val intent = Intent(this, RegisterActivity::class.java)

            // Menjalankan intent
            startActivity(intent)
        }

        mSLideViewPager = findViewById(R.id.slideViewPager)
        mDotLayout = findViewById(R.id.indicator_layout)

        viewPagerAdapter = ViewPagerAdapter(this)
        mSLideViewPager?.adapter = viewPagerAdapter
        setUpIndicator(0)
        mSLideViewPager?.addOnPageChangeListener(viewListener)
    }

    
    private fun setUpIndicator(position: Int) {
        val dotCount = descriptions.size
        val dotMargin = resources.getDimensionPixelSize(R.dimen.dot_margin)
        dots = Array(dotCount) { TextView(this) }
        mDotLayout?.removeAllViews()

        for (i in 0 until dotCount) {
            dots[i].text = Html.fromHtml("&#x2500;")
            dots[i].textSize = 35f
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.inactive))

            // Menetapkan margin untuk setiap titik
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(dotMargin, 0, dotMargin, 0)
            dots[i].layoutParams = params

            mDotLayout?.addView(dots[i])
        }

        dots[position].setTextColor(ContextCompat.getColor(this, R.color.active))

        // Set deskripsi sesuai dengan posisi
        val textDescription = findViewById<TextView>(R.id.tvDesc)
        textDescription.text = getString(descriptions[position])
    }


    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            setUpIndicator(position)

        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getitem(i: Int): Int {
        return mSLideViewPager?.currentItem ?: 0 + i
    }
}
