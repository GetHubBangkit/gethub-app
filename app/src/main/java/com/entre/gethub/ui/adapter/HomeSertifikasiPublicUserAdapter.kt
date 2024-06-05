package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.databinding.ItemHomeKelolamygethubSertifikasiPublicUserBinding

class HomeSertifikasiPublicUserAdapter(
    private var sertifikasiList: MutableList<UserPublicProfileResponse.Data.Certifications>,
    private val listener: (UserPublicProfileResponse.Data.Certifications, Int) -> Unit
) : RecyclerView.Adapter<HomeSertifikasiPublicUserAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubSertifikasiPublicUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(sertifikasiList[position])
    }

    override fun getItemCount(): Int = sertifikasiList.size

    fun setSertifikasiList(sertifikasiList: MutableList<UserPublicProfileResponse.Data.Certifications>) {
        this.sertifikasiList = sertifikasiList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val itemSertifikasiBinding: ItemHomeKelolamygethubSertifikasiPublicUserBinding,
        private val listener: (UserPublicProfileResponse.Data.Certifications, Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemSertifikasiBinding.root) {

        fun bindItem(sertifikasi: UserPublicProfileResponse.Data.Certifications) {
            with(itemSertifikasiBinding) {
                Glide.with(itemView)
                    .load(sertifikasi.image)
                    .placeholder(R.drawable.ic_image)
                    .into(ivCertificationImage)
                tvCertificationTitle.text = sertifikasi.title
                tvKategori.text = sertifikasi.category_name
            }
        }
    }




}
