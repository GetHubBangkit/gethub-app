package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.ProjectBid
import com.entre.gethub.databinding.ItemProjectRekomendasijobbidBinding


class HomeProjectBidsAdapter(

    private val ProjectBidsList: ArrayList<ProjectBid>,
    private val listener: (ProjectBid, Int) -> Unit
) :
    RecyclerView.Adapter<HomeProjectBidsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemProjectRekomendasijobbidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(ProjectBidsList[position])
        holder.itemView.setOnClickListener { listener(ProjectBidsList[position], position) }
    }

    override fun getItemCount(): Int {
        return ProjectBidsList.size
    }

    class ViewHolder(private val itemBinding: ItemProjectRekomendasijobbidBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(ProjectBids: ProjectBid) {
            itemBinding.profilepic2.setImageResource(ProjectBids.profilepic2)
            itemBinding.rekomendasiprofilename.text = ProjectBids.rekomendasiprofilename
            itemBinding.rekomendasiprofiledesc.text = ProjectBids.rekomendasiprofiledesc
            itemBinding.rekrutproject.text = ProjectBids.rekrutproject
            itemBinding.rekrutprice.text = ProjectBids.rekrutprice
            itemBinding.rekrutprojectdesc.text = ProjectBids.rekrutprojectdesc
            itemBinding.rekrutprojecttotal.text = ProjectBids.rekrutprojecttotal
            itemBinding.rekrutprojectdate.text = ProjectBids.rekrutprojectdate
            itemBinding.rekrutprojectdeadline.text = ProjectBids.rekrutprojectdeadline


        }
    }

}
