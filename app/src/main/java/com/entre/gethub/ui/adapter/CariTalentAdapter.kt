package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.databinding.ItemHomeCariTalentBinding
import com.entre.gethub.ui.models.CariTalent

class CariTalentAdapter(
    private val cariTalentList: MutableList<CariTalentResponse.Data>, // Ubah tipe data menjadi MutableList
    private val listener: (CariTalentResponse.Data, Int) -> Unit
) : RecyclerView.Adapter<CariTalentAdapter.ViewHolder>() {

    // Metode untuk menambahkan data ke daftar
    fun addAll(data: List<CariTalentResponse.Data>) {
        cariTalentList.clear() // Kosongkan daftar sebelum menambahkan data baru
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
        holder.itemView.setOnClickListener { listener(cariTalentList[position], position) }
    }

    override fun getItemCount(): Int {
        return cariTalentList.size
    }

    inner class ViewHolder(private val binding: ItemHomeCariTalentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cariTalent: CariTalentResponse.Data) {
            binding.apply {
                tvName.text = cariTalent.fullName
                tvProfesi.text = cariTalent.profession
            }
        }
    }
}
