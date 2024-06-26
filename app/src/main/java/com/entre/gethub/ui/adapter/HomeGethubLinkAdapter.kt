package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.databinding.ItemHomeKelolamygethubLinkBinding
import com.entre.gethub.ui.models.GethubLink

class HomeGethubLinkAdapter(
    private var gethubLinks: MutableList<GethubLink>,
    private val itemClickListener: (GethubLink, Int) -> Unit,
    private val deleteClickListener: (String) -> Unit
) : RecyclerView.Adapter<HomeGethubLinkAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener, deleteClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = gethubLinks[position]
        holder.bindItem(currentItem)
    }

    fun removeGethubLink(linkId: String) {
        val position = gethubLinks.indexOfFirst { it.id == linkId }
        if (position != -1) {
            gethubLinks.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    override fun getItemCount(): Int = gethubLinks.size

    fun updateGethubLinks(newLinks: List<GethubLink>) {
        gethubLinks.clear()
        gethubLinks.addAll(newLinks)
        notifyDataSetChanged()
    }


    class ViewHolder(
        private val binding: ItemHomeKelolamygethubLinkBinding,
        private val itemClickListener: (GethubLink, Int) -> Unit,
        private val deleteClickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(gethublink: GethubLink) {
            // Load image from URL using Glide
            Glide.with(binding.root.context)
                .load(gethublink.image)
                .into(binding.image)

            // Set up click listener for the link
            binding.root.setOnClickListener {
                itemClickListener(gethublink, bindingAdapterPosition)
            }

            // Set up click listener for the delete button
            binding.ivBin.setOnClickListener {
                deleteClickListener(gethublink.id)
            }
        }
    }
}
