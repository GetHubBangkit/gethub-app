package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.databinding.ItemHomeCariTalentBinding
import com.entre.gethub.ui.models.CariTalent
class CariTalentAdapter(
    private val cariTalentList: MutableList<CariTalentResponse.Data>,
    private val listener: (CariTalentResponse.Data) -> Unit
) : RecyclerView.Adapter<CariTalentAdapter.ViewHolder>() {

    fun addAll(data: List<CariTalentResponse.Data>) {
        cariTalentList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeCariTalentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cariTalentList[position])
    }

    override fun getItemCount(): Int {
        return cariTalentList.size
    }

    inner class ViewHolder(private val binding: ItemHomeCariTalentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val cariTalent = cariTalentList[position]
                    listener(cariTalent)
                }
            }
        }

        fun bind(cariTalent: CariTalentResponse.Data) {
            binding.apply {
                tvName.text = cariTalent.fullName
                tvProfesi.text = cariTalent.profession
            }
        }
    }
}
