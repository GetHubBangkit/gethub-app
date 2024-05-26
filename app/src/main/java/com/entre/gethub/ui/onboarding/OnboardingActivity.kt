package com.entre.gethub.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.entre.gethub.R
import com.entre.gethub.ui.auth.LoginActivity
import com.entre.gethub.ui.auth.RegisterActivity

class OnboardingActivity : AppCompatActivity() {
    private var mSLideViewPager: ViewPager? = null
    private var mDotLayout: LinearLayout? = null
    private lateinit var dots: Array<TextView>
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var backPressedTime: Long = 0

    private val descriptions = arrayOf(
        R.string.onboarding_desc_one,
        R.string.onboarding_desc_two
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Mendapatkan referensi ke tombol Masuk
        val masukButton: Button = findViewById(R.id.button_masuk)

        // Menambahkan listener klik pada tombol Masuk
        masukButton.setOnClickListener {
            // Membuat Intent untuk LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            // Menjalankan intent
            startActivity(intent)
        }

        // Mendapatkan referensi ke tombol Daftar
        val daftarButton: Button = findViewById(R.id.button_daftar)

        // Menambahkan listener klik pada tombol Daftar
        daftarButton.setOnClickListener {
            // Membuat Intent untuk RegisterActivity
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

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
