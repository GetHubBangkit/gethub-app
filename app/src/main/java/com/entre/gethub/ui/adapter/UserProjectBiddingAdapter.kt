package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.databinding.ItemUserProjectbidBinding
import com.entre.gethub.ui.models.UserProjectBidding


class UserProjectBiddingAdapter(
    private val userprojectbiddingList: List<UserProjectBidding>,
    private val listener: (UserProjectBidding, Int) -> Unit
) :
    RecyclerView.Adapter<UserProjectBiddingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            ItemUserProjectbidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(userprojectbiddingList[position])
        holder.itemView.setOnClickListener { listener(userprojectbiddingList[position], position) }
    }

    override fun getItemCount(): Int {
        return userprojectbiddingList.size
    }

    class ViewHolder(private val binding: ItemUserProjectbidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(user: UserProjectBidding) {
            Glide.with(binding.root.context)
                .load(user.photo)
                .placeholder(R.drawable.profilepic1)
                .into(binding.profilepic1)
            binding.profilename.text = user.fullName
            binding.profiledesc.text = user.profession

        }
    }
}
