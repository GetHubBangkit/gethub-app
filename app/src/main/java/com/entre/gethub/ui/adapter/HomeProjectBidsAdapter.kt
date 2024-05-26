package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.ui.models.ProjectBid
import com.entre.gethub.databinding.ItemProjectRekomendasijobbidBinding


class HomeProjectBidsAdapter(

    private val projectBidList: ArrayList<ProjectBid>,
    private val listener: (ProjectBid, Int) -> Unit
) :
    RecyclerView.Adapter<HomeProjectBidsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemProjectRekomendasijobbidBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(projectBidList[position])
        holder.itemView.setOnClickListener { listener(projectBidList[position], position) }
    }

    override fun getItemCount(): Int {
        return projectBidList.size
    }

    class ViewHolder(private val binding: ItemProjectRekomendasijobbidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(projectBid: ProjectBid) {
            with(binding) {
                ivProjectOwnerPic.setImageResource(projectBid.profilepic2)
                tvProjectOwnerName.text = projectBid.rekomendasiprofilename
                tvProjectOwnerProfession.text = projectBid.rekomendasiprofiledesc
                tvProjectTitle.text = projectBid.rekrutproject
                tvProjectPriceRange.text = projectBid.rekrutprice
                tvProjectDesc.text = projectBid.rekrutprojectdesc
                tvProjectTotalUserBids.text = projectBid.rekrutprojecttotal
                tvProjectPostDate.text = projectBid.rekrutprojectdate
                tvProjectDeadline.text = projectBid.rekrutprojectdeadline

            }
        }
    }

}
