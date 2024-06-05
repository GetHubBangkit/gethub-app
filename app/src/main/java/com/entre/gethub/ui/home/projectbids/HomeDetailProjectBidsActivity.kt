package com.entre.gethub.ui.home.projectbids

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ItemDetailProjectbidsBinding
import com.entre.gethub.ui.adapter.UserProjectBiddingAdapter
import com.entre.gethub.ui.models.UserProjectBidding
import com.entre.gethub.utils.Formatter
import com.entre.gethub.utils.ViewModelFactory

class HomeDetailProjectBidsActivity : AppCompatActivity() {
    private lateinit var binding: ItemDetailProjectbidsBinding
    private val homeDetailProjectBidsViewModel by viewModels<HomeDetailProjectBidsViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectBidId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailProjectbidsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectBidId = intent.getStringExtra(EXTRA_PROJECT_ID)

        projectBidId.let {
            getDetailProject(it!!)
        }

        binding.iconBack.setOnClickListener {
            finish()
        }

        binding.cvLihatMilestone.setOnClickListener {
            val intent = Intent(this, HomeMilestoneProjectBidsActivity::class.java).apply {
                putExtra(HomeMilestoneProjectBidsActivity.EXTRA_PROJECT_ID, projectBidId)
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        projectBidId = null
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
                            val userProjectBiddingList = projectBid.usersBid.map { usersBidItem ->
                                UserProjectBidding(
                                    usersBidItem.fullName!!,
                                    usersBidItem.photo!!,
                                    usersBidItem.profession!!
                                )
                            }
                            setupRecyclerViewUserBidding(userProjectBiddingList)
                        }

                        with(binding) {
                            val minBudget = Formatter.formatRupiah(projectBid.minBudget ?: 0)
                            val maxBudget = Formatter.formatRupiah(projectBid.maxBudget ?: 0)

                            tvDetailProjectCategory.text = projectBid.category?.name.toString()

                            tvDetailProjectTitle.text = projectBid.title
                            tvDetailProjectDesc.text = projectBid.description
                            tvDetailProjectPriceRange.text =
                                "$minBudget - $maxBudget"
                            tvDetailProjectDateRange.text =
                                "${projectBid?.minDeadline} - ${projectBid?.maxDeadline}"
                            tvDetailProjectDatePost.text = projectBid.createdDate
                            tvDetailProjectTotalUserBidsOnCard.text =
                                projectBid.totalBidders.toString()
                            tvDetailProjectTotalUserBids.text = projectBid.totalBidders.toString()
                            tvDetailProjectStatus.text = projectBid.statusProject

                            showOwnerSentiment(
                                projectBid.ownerProject?.fullName!!,
                                projectBid.ownerProject.sentimentOwnerAnalisis ?: "Netral"
                            )
                            tvDetailProjectOwnerName.text = projectBid.ownerProject.fullName
                            tvDetailProjectOwnerProfession.text =
                                projectBid.ownerProject.profession
                            Glide.with(this@HomeDetailProjectBidsActivity)
                                .load(projectBid.ownerProject.photo)
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

    private fun showOwnerSentiment(projectOwnerName: String, sentiment: String?) {
        val projectOwnerSentiment = binding.tvDetailProjectOwnerSentiment

        var teks: String =
            "Berdasarkan history review Kusnandar sebagai pemberi project job memiliki penilaian sentimen analisis Netral"

        if (sentiment == "Netral") {
            binding.cvSentiment.setCardBackgroundColor(getColor(R.color.color_sentiment_neutral))
            teks =
                "Berdasarkan history review $projectOwnerName sebagai pemberi project job memiliki penilaian sentimen analisis $sentiment"
        } else if (sentiment == "Positif") {
            binding.cvSentiment.setCardBackgroundColor(getColor(R.color.color_sentiment_positive))
            teks =
                "Berdasarkan history review $projectOwnerName sebagai pemberi project job memiliki penilaian sentimen analisis $sentiment. Ayo ikuti project dan selesaikan pekerjaan kamu dan berikan hasil yang maksimal"
        } else {
            binding.cvSentiment.setCardBackgroundColor(getColor(R.color.color_sentiment_negative))
            teks =
                "Berdasarkan history review $projectOwnerName sebagai pemberi project job memiliki penilaian sentimen analisis $sentiment, berhati-hatilah dan jangan pernah memberikan informasi pribadi apapun"
        }

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
            val startIndexSentiment = teks.indexOf(sentiment.toString())
            val endIndexSentiment = startIndexSentiment + sentiment!!.length

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