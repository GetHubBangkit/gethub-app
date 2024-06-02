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
        fun bindItem(projectBid: MyProjectBidResponse.UsersBidItem) {
            with(binding) {
                val postDate = Formatter.formatPostDate(projectBid.project?.createdDate!!)
                val minBudget = Formatter.formatRupiah(projectBid.project.minBudget ?: 0)
                val maxBudget = Formatter.formatRupiah(projectBid.project.maxBudget ?: 0)

                // Project Owner
                Glide.with(itemView.context)
                    .load(projectBid.project.ownerProject?.photo)
                    .placeholder(R.drawable.profilepic2)
                    .into(ivProjectOwnerPic)
                tvProjectOwnerName.text = projectBid.project.ownerProject?.fullName
                tvProjectOwnerProfession.text = projectBid.project.ownerProject?.fullName

                // Project Data
                tvProjectTitle.text = projectBid.project.title
                tvProjectPriceRange.text =
                    "$minBudget - $maxBudget"
                tvProjectDesc.text = projectBid.project.description
                tvProjectTotalUserBids.text = "Total User Bids: 5 User"
                tvProjectPostDate.text = "Diunggah: $postDate"
                tvProjectDeadline.text = "Deadline: ${projectBid.project.deadlineDuration} Hari"
            }
        }

    }

}