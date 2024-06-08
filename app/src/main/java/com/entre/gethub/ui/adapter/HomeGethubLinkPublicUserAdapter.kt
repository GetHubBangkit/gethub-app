package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.databinding.ItemHomeKelolamygethubLinkPublicUserBinding
import com.entre.gethub.ui.models.GethubLink

class HomeGethubLinkPublicUserAdapter(
    private var gethubLinks: MutableList<GethubLink>,
    private val itemClickListener: (GethubLink, Int) -> Unit
) : RecyclerView.Adapter<HomeGethubLinkPublicUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubLinkPublicUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = gethubLinks[position]
        holder.bindItem(currentItem)
    }

    override fun getItemCount(): Int = gethubLinks.size

    fun updateGethubLinks(newLinks: List<GethubLink>) {
        gethubLinks.clear()
        gethubLinks.addAll(newLinks)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemHomeKelolamygethubLinkPublicUserBinding,
        private val itemClickListener: (GethubLink, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(gethublink: GethubLink) {
            Glide.with(binding.root.context)
                .load(gethublink.image)
                .into(binding.image)

            // Set up click listener for the link
            binding.root.setOnClickListener {
                itemClickListener(gethublink, bindingAdapterPosition)
            }
        }
    }
}
