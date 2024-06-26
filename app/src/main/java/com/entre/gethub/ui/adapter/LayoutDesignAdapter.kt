package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.databinding.ItemHomeKelolamygethubTemalayoutdesignBinding
import com.entre.gethub.ui.models.LayoutDesign

class LayoutDesignAdapter(
    private val layoutdesignList: ArrayList<LayoutDesign>,
    private val isPremiumUser: Boolean,
    private val listener: (LayoutDesign, Int) -> Unit
) : RecyclerView.Adapter<LayoutDesignAdapter.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubTemalayoutdesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = layoutdesignList[position]
        holder.bindItem(currentItem)

        // Highlight only if the selected position is this one and it's allowed to highlight
        if (selectedPosition == position && (isPremiumUser || position < 6)) {
            holder.itemView.alpha = 1.0f // Highlight selected item
        } else {
            holder.itemView.alpha = 0.5f // Dim unselected items
        }

        holder.itemView.setOnClickListener {
            if (isPremiumUser || position < 6) { // Allow selection for free designs or if user is premium
                selectedPosition = position
                notifyDataSetChanged()
                listener(currentItem, position)
            } else {
                listener(currentItem, position) // Notify listener for non-premium user trying to select a premium design
            }
        }
    }

    override fun getItemCount(): Int {
        return layoutdesignList.size
    }

    class ViewHolder(private val binding: ItemHomeKelolamygethubTemalayoutdesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(layoutdesign: LayoutDesign) {
            Glide.with(itemView.context)
                .load(layoutdesign.image)
                .into(binding.image)
        }
    }
}
