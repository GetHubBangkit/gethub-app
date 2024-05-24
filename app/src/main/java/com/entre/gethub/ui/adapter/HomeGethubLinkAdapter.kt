package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.databinding.ItemHomeKelolamygethubLinkBinding
import com.entre.gethub.ui.models.GetHubLink

class HomeGethubLinkAdapter(
    private var gethubLinks: List<GetHubLink>,
    private val itemClickListener: (GetHubLink, Int) -> Unit,
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

    override fun getItemCount(): Int = gethubLinks.size

    fun updateGethubLinks(newLinks: List<GetHubLink>) {
        gethubLinks = newLinks
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemHomeKelolamygethubLinkBinding,
        private val itemClickListener: (GetHubLink, Int) -> Unit,
        private val deleteClickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(gethublink: GetHubLink) {
            binding.image.setImageResource(gethublink.image)

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
