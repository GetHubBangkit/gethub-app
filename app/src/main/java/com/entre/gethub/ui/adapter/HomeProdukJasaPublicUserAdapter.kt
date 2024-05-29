package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.databinding.ItemHomeKelolamygethubProdukjasaPublicUserBinding

class HomeProdukJasaPublicUserAdapter(
    private var produkjasaList: MutableList<UserPublicProfileResponse.Data.Product>,
    private val listener: (UserPublicProfileResponse.Data.Product, Int) -> Unit
) : RecyclerView.Adapter<HomeProdukJasaPublicUserAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubProdukjasaPublicUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(produkjasaList[position])
    }

    override fun getItemCount(): Int = produkjasaList.size

    fun setProdukJasaList(produkjasaList: MutableList<UserPublicProfileResponse.Data.Product>) {
        this.produkjasaList = produkjasaList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val itemProdukJasaBinding: ItemHomeKelolamygethubProdukjasaPublicUserBinding,
        private val listener: (UserPublicProfileResponse.Data.Product, Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemProdukJasaBinding.root) {

        fun bindItem(produkjasa: UserPublicProfileResponse.Data.Product) {
            with(itemProdukJasaBinding) {
                Glide.with(itemView)
                    .load(produkjasa.imageUrl)
                    .placeholder(R.drawable.ic_image)
                    .into(ivProductImage)
                tvProductTitle.text = produkjasa.name
                tvProductDesc.text = produkjasa.description
            }
        }
    }




}
