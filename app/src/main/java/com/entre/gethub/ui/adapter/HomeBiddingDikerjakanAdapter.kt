package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.ui.models.BiddingDikerjakan
import com.entre.gethub.databinding.ItemHomeBiddingdikerjakanBinding


class HomeBiddingDikerjakanAdapter(
    private val biddingdikerjakanList: ArrayList<BiddingDikerjakan>,
    private val listener: (BiddingDikerjakan, Int) -> Unit
) :
    RecyclerView.Adapter<HomeBiddingDikerjakanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeBiddingdikerjakanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(biddingdikerjakanList[position])
        holder.itemView.setOnClickListener { listener(biddingdikerjakanList[position], position) }
    }

    override fun getItemCount(): Int {
        return biddingdikerjakanList.size
    }

    class ViewHolder(var ItemHomeBiddingdikerjakanBinding: ItemHomeBiddingdikerjakanBinding) :
        RecyclerView.ViewHolder(ItemHomeBiddingdikerjakanBinding.root) {
        fun bindItem(biddingdikerjakan: BiddingDikerjakan) {
//            ItemHomeBiddingdikerjakanBinding.frame1.setImageResource(biddingdikerjakan.image)
            ItemHomeBiddingdikerjakanBinding.profilepic2.setImageResource(biddingdikerjakan.profilepic)
            ItemHomeBiddingdikerjakanBinding.rekomendasiprofilename.text = biddingdikerjakan.profilename
            ItemHomeBiddingdikerjakanBinding.rekomendasiprofiledesc.text = biddingdikerjakan.profiledesc
            ItemHomeBiddingdikerjakanBinding.rekrutproject.text = biddingdikerjakan.projecttitle
            ItemHomeBiddingdikerjakanBinding.rekrutprice.text = biddingdikerjakan.projectprice
            ItemHomeBiddingdikerjakanBinding.rekrutprojectdeadline.text = biddingdikerjakan.deadline

        }
    }
}
