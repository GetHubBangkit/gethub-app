package com.entre.gethub.ui.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.PostedProjectDetailResponse
import com.entre.gethub.databinding.ItemSelectUserBiddingBinding
import com.entre.gethub.utils.Formatter

class SelectUserBiddingAdapter(
    private val userBiddingList: List<PostedProjectDetailResponse.UsersBidItem>,
    private val onSelectUserBid: (PostedProjectDetailResponse.UsersBidItem) -> Unit,
    private val onClickUserBid: (PostedProjectDetailResponse.UsersBidItem) -> Unit,
) : RecyclerView.Adapter<SelectUserBiddingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            ItemSelectUserBiddingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(userBiddingList[position], onSelectUserBid)
        holder.itemView.setOnClickListener { onClickUserBid(userBiddingList[position]) }
    }

    override fun getItemCount(): Int = userBiddingList.size

    class ViewHolder(private val binding: ItemSelectUserBiddingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(
            userBidding: PostedProjectDetailResponse.UsersBidItem,
            onSelectUserBid: (PostedProjectDetailResponse.UsersBidItem) -> Unit
        ) {
            with(binding) {
                val budgetBid = Formatter.formatRupiah(userBidding.budgetBid)

                tvUserName.text = userBidding.fullName
                tvUserJobName.text = userBidding.profession
                Glide.with(binding.root.context)
                    .load(userBidding.photo)
                    .placeholder(R.drawable.profilepic1)
                    .into(ivUserProfile)
                tvProjectAmount.text = budgetBid
                tvMessage.text = userBidding.message

                if (userBidding.isSelected == true) {
                    tvDetailProjectStatus.text = "Terpilih"
                    tvDetailProjectStatus.setTextColor(
                        getColor(
                            binding.root.context,
                            R.color.black
                        )
                    )
                    cvSelect.setCardBackgroundColor(
                        getColor(
                            binding.root.context,
                            R.color.color_sentiment_neutral
                        )
                    )
                } else {
                    cvSelect.setOnClickListener { onSelectUserBid(userBidding) }
                }


                showUserSentiment(userBidding.fullName.toString(), sentiment = userBidding.sentimentFreelanceAnalisis.toString())
            }
        }

        private fun showUserSentiment(projectUserName: String, sentiment: String) {
            val projectOwnerSentiment = binding.tvDetailProjectOwnerSentiment

            if (sentiment == "Netral") {
                binding.cvSentiment.setCardBackgroundColor(
                    getColor(
                        binding.root.context,
                        R.color.color_sentiment_neutral
                    )
                )
            } else if (sentiment == "Positif") {
                binding.cvSentiment.setCardBackgroundColor(
                    getColor(
                        binding.root.context,
                        R.color.color_sentiment_positive
                    )
                )
            } else {
                binding.cvSentiment.setCardBackgroundColor(
                    getColor(
                        binding.root.context,
                        R.color.color_sentiment_negative
                    )
                )
            }

            // Buat teks untuk ditampilkan
            val teks =
                "Berdasarkan history review $projectUserName sebagai penerima project job memiliki penilaian sentimen analisis $sentiment"

            // Set teks ke TextView
            projectOwnerSentiment.text = teks

            // Buat objek Typeface dengan Nunito-Black
            val nunitoBlackTypeface =
                ResourcesCompat.getFont(binding.root.context, R.font.nunito_black)

            if (nunitoBlackTypeface != null) {
                // Buat spannable string untuk mengatur jenis font
                val spannableString = SpannableString(teks)

                // Temukan indeks teks "name" dan "sentiment" dalam teks
                val startIndexName = teks.indexOf(projectUserName)
                val endIndexName = startIndexName + projectUserName.length
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
    }

}