package com.entre.gethub.ui.home.projectbids

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.databinding.ItemDetailProjectbidsBinding
import com.entre.gethub.ui.adapter.UserProjectBiddingAdapter
import com.entre.gethub.ui.models.ProjectBid
import com.entre.gethub.ui.models.UserProjectBidding

class HomeDetailProjectBidsActivity : AppCompatActivity() {
    private lateinit var binding: ItemDetailProjectbidsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailProjectbidsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve ProjectBid object from intent
        val projectBid = intent.getParcelableExtra<ProjectBid>("project_bid")

        // Check if projectBid is not null before using it
        projectBid?.let {
            with(binding) {
                tvDetailProjectOwnerName.text = it.rekomendasiprofilename
                tvDetailProjectOwnerProfession.text = it.rekomendasiprofiledesc
                tvDetailProjectTitle.text = it.rekrutproject
                tvDetailProjectDateRange.text = it.rekrutprice
                tvDetailProjectDesc.text = it.rekrutprojectdesc
                tvDetailProjectTotalUserBids.text = it.rekrutprojecttotal
                tvDetailProjectDatePost.text = it.rekrutprojectdate
                tvDetailProjectTotalUserBidsOnCard.text = it.rekrutprojecttotal
                ivDetailProjectOwnerPic.setImageResource(it.profilepic2)
            }
        }

        binding.iconBack.setOnClickListener {
            finish()
        }

        binding.btnIkutBidding.setOnClickListener {
            val intent = Intent(this, HomeDetailProjectBidsFormActivity::class.java)
            intent.putExtra("rekrutprice", projectBid?.rekrutprice)
            startActivity(intent)
        }
        setupRecyclerViewUserBidding()


        // Dapatkan TextView
        val teksanalisis: TextView = findViewById(R.id.teksanalisis)

        // Ambil nilai name dari rekomendasiprofilename
        val name = binding.tvDetailProjectOwnerName.text.toString()

        // Variabel untuk diisi sentimen analisis
        val sentiment = "Positive" // Ganti dengan nilai yang sesuai

        // Buat teks untuk ditampilkan
        val teks =
            "Berdasarkan history review $name sebagai pemberi project job memiliki penilaian sentimen analisis $sentiment, ayo ikuti project dan selesaikan pekerjaan kamu dan berikan hasil yg maksimal"

        // Set teks ke TextView
        teksanalisis.text = teks

        // Buat objek Typeface dengan Nunito-Black
        val nunitoBlackTypeface = ResourcesCompat.getFont(this, R.font.nunito_black)

        if (nunitoBlackTypeface != null) {
            // Buat spannable string untuk mengatur jenis font
            val spannableString = SpannableString(teks)

            // Temukan indeks teks "name" dan "sentiment" dalam teks
            val startIndexName = teks.indexOf(name)
            val endIndexName = startIndexName + name.length
            val startIndexSentiment = teks.indexOf(sentiment)
            val endIndexSentiment = startIndexSentiment + sentiment.length

            // Terapkan jenis font Nunito-Black ke bagian "name"
            spannableString.setSpan(
                CustomTypefaceSpan(nunitoBlackTypeface),
                startIndexName,
                endIndexName,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Terapkan jenis font Nunito-Black ke bagian "sentiment"
            spannableString.setSpan(
                CustomTypefaceSpan(nunitoBlackTypeface),
                startIndexSentiment,
                endIndexSentiment,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Terapkan teks dengan jenis font yang sudah diatur
            teksanalisis.text = spannableString
        } else {
            // Font tidak ditemukan, lakukan penanganan kesalahan
            Toast.makeText(this, "Failed to load font", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupRecyclerViewUserBidding() {
        binding.rvDetailProjectBidUser.apply {
            layoutManager = LinearLayoutManager(
                this@HomeDetailProjectBidsActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter =
                UserProjectBiddingAdapter(createUserProjectBiddingList()) { userprojectbidding, position ->
                    Toast.makeText(
                        this@HomeDetailProjectBidsActivity, // Gunakan requireContext() untuk mendapatkan Context yang benar
                        "Clicked on actor: ${userprojectbidding.profilename}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun createUserProjectBiddingList(): ArrayList<UserProjectBidding> {
        return arrayListOf<UserProjectBidding>(
            UserProjectBidding(
                "Tono Jaya",
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            UserProjectBidding(
                "Tono Jaya",
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            UserProjectBidding(
                "Tono Jaya",
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            UserProjectBidding(
                "Tono Jaya",
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            UserProjectBidding(
                "Tono Jaya",
                R.drawable.profilepic1,
                "Software Enginer"
            ),

            )
    }

    private class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
        override fun updateMeasureState(textPaint: TextPaint) {
            textPaint.typeface = typeface
        }

        override fun updateDrawState(tp: TextPaint) {
            tp.typeface = typeface
        }
    }
}