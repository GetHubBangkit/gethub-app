package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.databinding.ItemHomeKelolamygethubLinkBinding
import com.entre.gethub.ui.models.GethubLink

class HomeGethubLinkPublicUserAdapter(
    private var gethubLinks: MutableList<GethubLink>,
    private val itemClickListener: (GethubLink, Int) -> Unit
) : RecyclerView.Adapter<HomeGethubLinkPublicUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val binding: ItemHomeKelolamygethubLinkBinding,
        private val itemClickListener: (GethubLink, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(gethublink: GethubLink) {
            binding.image.setImageResource(gethublink.image)

            // Set up click listener for the link
            binding.root.setOnClickListener {
                itemClickListener(gethublink, bindingAdapterPosition)
            }
        }
    }
}
