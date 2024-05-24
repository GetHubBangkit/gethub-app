package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.databinding.ItemUserProjectbidBinding
import com.entre.gethub.ui.models.UserProjectBidding


class UserProjectBiddingAdapter(
    private val userprojectbiddingList: ArrayList<UserProjectBidding>,
    private val listener: (UserProjectBidding, Int) -> Unit
) :
    RecyclerView.Adapter<UserProjectBiddingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemUserProjectbidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(userprojectbiddingList[position])
        holder.itemView.setOnClickListener { listener(userprojectbiddingList[position], position) }
    }

    override fun getItemCount(): Int {
        return userprojectbiddingList.size
    }

    class ViewHolder(var ItemDetailprojectbidsBinding: ItemUserProjectbidBinding) :
        RecyclerView.ViewHolder(ItemDetailprojectbidsBinding.root) {
        fun bindItem(userprojectbidding: UserProjectBidding) {

            ItemDetailprojectbidsBinding.profilepic1.setImageResource(userprojectbidding.profilepic)
            ItemDetailprojectbidsBinding.profilename.text = userprojectbidding.profilename
            ItemDetailprojectbidsBinding.profiledesc.text = userprojectbidding.profiledesc

        }
    }
}
