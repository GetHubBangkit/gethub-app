package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.Category
import com.entre.gethub.data.remote.response.certifications.Certification
import com.entre.gethub.databinding.ItemHomeKelolamygethubSertifikasiBinding

class HomeSertifikasiAdapter(
    private val sertifikasiList: MutableList<Certification>,
    private val categories: List<Category>,
    private val listener: (Certification, Int) -> Unit
) : RecyclerView.Adapter<HomeSertifikasiAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeKelolamygethubSertifikasiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v, onItemClickCallback, categories)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(sertifikasiList[position])
        holder.itemView.setOnClickListener { listener(sertifikasiList[position], position) }
    }

    override fun getItemCount(): Int {
        return sertifikasiList.size
    }

    class ViewHolder(
        private val itemHomeKelolamygethubSertifikasiBinding: ItemHomeKelolamygethubSertifikasiBinding,
        private val onItemClickCallback: OnItemClickCallback,
        private val categories: List<Category>
    ) : RecyclerView.ViewHolder(itemHomeKelolamygethubSertifikasiBinding.root) {

        fun bindItem(sertifikasi: Certification) {
            with(itemHomeKelolamygethubSertifikasiBinding) {
                Glide.with(itemView)
                    .load(sertifikasi.image)
                    .placeholder(R.drawable.ic_image)
                    .into(ivCertificationImage)
                tvCertificationTitle.text = sertifikasi.title

                val category = categories.find { it.id == sertifikasi.categoryId }
                tvKategori.text = category?.name ?: "Unknown Category"

                ivDeleteCertification.setOnClickListener {
                    onItemClickCallback.onDeleteCertificationItem(sertifikasi, adapterPosition)
                }
            }
        }
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        this.onItemClickCallback = callback
    }

    interface OnItemClickCallback {
        fun onDeleteCertificationItem(certification: Certification, position: Int)
    }

    // Tambahkan fungsi untuk menghapus item
    fun removeSertifikasi(id: String) {
        val position = sertifikasiList.indexOfFirst { it.id == id }
        if (position != -1) {
            sertifikasiList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
