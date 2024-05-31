package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.PostedProjectDetailResponse
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.databinding.ItemPostedProjectBinding
import com.entre.gethub.databinding.ItemSelectUserBiddingBinding
import com.entre.gethub.utils.Formatter

class SelectUserBiddingAdapter(
    private val userBiddingList: List<PostedProjectDetailResponse.UsersBidItem>,
    private val onSelectUserBid: (PostedProjectDetailResponse.UsersBidItem) -> Unit,
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
        holder.bindItem(userBiddingList[position])
        holder.selectUserBid { }
    }

    override fun getItemCount(): Int = userBiddingList.size

    class ViewHolder(private val binding: ItemSelectUserBiddingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(userBidding: PostedProjectDetailResponse.UsersBidItem) {
            with(binding) {
                tvUserName.text = userBidding.fullName
                tvUserJobName.text = userBidding.profession
                Glide.with(binding.root.context)
                    .load(userBidding.photo)
                    .placeholder(R.drawable.profilepic1)
                    .into(ivUserProfile)
                tvProjectAmount.text = "Rupiah coy"
                tvMessage.text = "Pesan coy"
            }
        }

        fun selectUserBid(onClick: (PostedProjectDetailResponse.UsersBidItem) -> Unit) {
            binding.cvSelect.setOnClickListener {
                onClick
            }
        }
    }
}