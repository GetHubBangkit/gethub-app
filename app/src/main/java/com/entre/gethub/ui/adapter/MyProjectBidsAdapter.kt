package com.entre.gethub.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.MyProjectBidResponse
import com.entre.gethub.databinding.ItemProjectRekomendasijobbidBinding
import com.entre.gethub.utils.Formatter

class MyProjectBidsAdapter(
    private val myProjectBidList: List<MyProjectBidResponse.UsersBidItem>,
    private val listener: (MyProjectBidResponse.UsersBidItem, Int) -> Unit
) : RecyclerView.Adapter<MyProjectBidsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = ItemProjectRekomendasijobbidBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(myProjectBidList[position])
        holder.itemView.setOnClickListener { listener(myProjectBidList[position], position) }
    }

    override fun getItemCount(): Int = myProjectBidList.size

    class ViewHolder(private val binding: ItemProjectRekomendasijobbidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(userBid: MyProjectBidResponse.UsersBidItem) {
            with(binding) {
                val postDate = Formatter.formatPostDate(userBid.project?.createdDate!!)
                val minBudget = Formatter.formatRupiah(userBid.project.minBudget ?: 0)
                val maxBudget = Formatter.formatRupiah(userBid.project.maxBudget ?: 0)

                // Project Owner
                Glide.with(itemView.context)
                    .load(userBid.project.ownerProject?.photo)
                    .placeholder(R.drawable.profilepic2)
                    .into(ivProjectOwnerPic)
                tvProjectOwnerName.text = userBid.project.ownerProject?.fullName
                tvProjectOwnerProfession.text = userBid.project.ownerProject?.profession

                // Project Data
                tvProjectTitle.text = userBid.project.title
                tvProjectPriceRange.text =
                    "$minBudget - $maxBudget"
                tvProjectDesc.text = userBid.project.description
                tvProjectTotalUserBids.text = "Total User Bids: ${userBid.project.totalBidders} User"
                tvProjectPostDate.text = "Diunggah: $postDate"
                tvProjectDeadline.text = "Deadline: ${userBid.project.deadlineDuration} Hari"
            }
        }

    }

}