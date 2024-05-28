package com.entre.gethub.ui.home.projectbids

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ItemDetailProjectbidsBinding
import com.entre.gethub.ui.adapter.UserProjectBiddingAdapter
import com.entre.gethub.ui.models.ProjectBid
import com.entre.gethub.ui.models.UserProjectBidding
import com.entre.gethub.utils.ViewModelFactory

class HomeDetailProjectBidsActivity : AppCompatActivity() {
    private lateinit var binding: ItemDetailProjectbidsBinding
    private val homeDetailProjectBidsViewModel by viewModels<HomeDetailProjectBidsViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailProjectbidsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projectBidId = intent.getStringExtra(EXTRA_PROJECT_ID)

        getDetailProject(projectBidId!!)

        binding.iconBack.setOnClickListener {
            finish()
        }

    }

    private fun getDetailProject(id: String) {
        homeDetailProjectBidsViewModel.getProjectDetail(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnCard(true)
                    is Result.Success -> {
                        showLoadingOnCard(false)
                        val projectBid = result.data.data

                        if (projectBid?.totalBidders!! == 0) {
                            showEmptyOnUserBidding(true, "Belum Ada User Yang Ikut Bidding")
                        } else {
                            val userProjectBiddingList = projectBid.usersBid?.map { usersBidItem ->
                                UserProjectBidding(
                                    usersBidItem?.fullName!!,
                                    usersBidItem?.photo!!,
                                    usersBidItem?.profession!!
                                )
                            }
                            setupRecyclerViewUserBidding(userProjectBiddingList!!)
                        }

                        with(binding) {
                            tvDetailProjectTitle.text = projectBid?.title
                            tvDetailProjectDesc.text = projectBid?.description
                            tvDetailProjectPriceRange.text =
                                "Rp ${projectBid?.minBudget} - Rp ${projectBid?.maxBudget}"
                            tvDetailProjectDateRange.text =
                                "${projectBid?.minDeadline} - ${projectBid?.maxDeadline}"
                            tvDetailProjectDatePost.text = projectBid?.createdDate
                            tvDetailProjectTotalUserBidsOnCard.text =
                                projectBid?.totalBidders.toString()
                            tvDetailProjectTotalUserBids.text = projectBid?.totalBidders.toString()
                            tvDetailProjectStatus.text = projectBid?.statusProject

                            showOwnerSentiment(projectBid?.ownerProject?.fullName!!)
                            tvDetailProjectOwnerName.text = projectBid?.ownerProject?.fullName
                            tvDetailProjectOwnerProfession.text =
                                projectBid?.ownerProject?.profession
                            Glide.with(this@HomeDetailProjectBidsActivity)
                                .load(projectBid?.ownerProject?.photo)
                                .placeholder(R.drawable.profilepic2)
                                .into(ivDetailProjectOwnerPic)
                        }

                        binding.btnIkutBidding.setOnClickListener {
                            val intent =
                                Intent(this, HomeDetailProjectBidsFormActivity::class.java)
                            intent.putExtra(
                                HomeDetailProjectBidsFormActivity.EXTRA_MIN_BUDGET,
                                projectBid.minBudget
                            )
                            intent.putExtra(
                                HomeDetailProjectBidsFormActivity.EXTRA_MAX_BUDGET,
                                projectBid.maxBudget
                            )
                            intent.putExtra(
                                HomeDetailProjectBidsFormActivity.EXTRA_PROJECT_ID,
                                projectBid.id
                            )
                            startActivity(intent)
                        }
                    }

                    is Result.Error -> {
                        showLoadingOnCard(false)
                        showToast(result.error)
                    }

                    is Result.Empty -> {

                    }
                }
            }
        }
    }


    private fun setupRecyclerViewUserBidding(userProjectBiddingList: List<UserProjectBidding>) {
        binding.rvDetailProjectBidUser.apply {
            layoutManager = LinearLayoutManager(
                this@HomeDetailProjectBidsActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter =
                UserProjectBiddingAdapter(userProjectBiddingList) { user, _ ->
                    Toast.makeText(
                        this@HomeDetailProjectBidsActivity, // Gunakan requireContext() untuk mendapatkan Context yang benar
                        "Clicked on actor: ${user.fullName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun showOwnerSentiment(projectOwnerName: String) {
        val projectOwnerSentiment = binding.tvDetailProjectOwnerSentiment

        // Variabel untuk diisi sentimen analisis
        val sentiment = "Positive" // Ganti dengan nilai yang sesuai

        if (sentiment == "Netral") {
            binding.cvSentiment.setCardBackgroundColor(getColor(R.color.color_sentiment_neutral))
        } else if (sentiment == "Positive") {
            binding.cvSentiment.setCardBackgroundColor(getColor(R.color.color_sentiment_positive))
        } else {
            binding.cvSentiment.setCardBackgroundColor(getColor(R.color.color_sentiment_negative))
        }

        // Buat teks untuk ditampilkan
        val teks =
            "Berdasarkan history review $projectOwnerName sebagai pemberi project job memiliki penilaian sentimen analisis $sentiment, ayo ikuti project dan selesaikan pekerjaan kamu dan berikan hasil yg maksimal"

        // Set teks ke TextView
        projectOwnerSentiment.text = teks

        // Buat objek Typeface dengan Nunito-Black
        val nunitoBlackTypeface = ResourcesCompat.getFont(this, R.font.nunito_black)

        if (nunitoBlackTypeface != null) {
            // Buat spannable string untuk mengatur jenis font
            val spannableString = SpannableString(teks)

            // Temukan indeks teks "name" dan "sentiment" dalam teks
            val startIndexName = teks.indexOf(projectOwnerName)
            val endIndexName = startIndexName + projectOwnerName.length
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
            projectOwnerSentiment.text = spannableString
        }
    }

    private class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
        override fun updateMeasureState(textPaint: TextPaint) {
            textPaint.typeface = typeface
        }

        override fun updateDrawState(tp: TextPaint) {
            tp.typeface = typeface
        }
    }

    private fun showLoadingOnCard(isLoading: Boolean) {
        binding.progressBarOnCard.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyOnUserBidding(isEmpty: Boolean, message: String) {
        binding.tvEmptyUserBidding.text = message
        binding.tvEmptyUserBidding.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@HomeDetailProjectBidsActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
    }
}