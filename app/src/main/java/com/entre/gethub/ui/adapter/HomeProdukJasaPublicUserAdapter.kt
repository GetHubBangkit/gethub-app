package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.databinding.ItemHomeKelolamygethubProdukjasaBinding

class HomeProdukJasaPublicUserAdapter(
    private var produkjasaList: MutableList<UserPublicProfileResponse.Data.Product>,
    private val listener: (UserPublicProfileResponse.Data.Product, Int) -> Unit
) : RecyclerView.Adapter<HomeProdukJasaPublicUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubProdukjasaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(produkjasaList[position])
        holder.itemView.setOnClickListener { listener(produkjasaList[position], position) }
    }

    override fun getItemCount(): Int = produkjasaList.size

    fun setProdukJasaList(produkjasaList: MutableList<UserPublicProfileResponse.Data.Product>) {
        this.produkjasaList = produkjasaList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val itemProdukJasaBinding: ItemHomeKelolamygethubProdukjasaBinding,
        private val listener: (UserPublicProfileResponse.Data.Product, Int) -> Unit,
        private val onItemClickCallback: OnItemClickCallback
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

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        this.onItemClickCallback = callback
    }

    interface OnItemClickCallback {
        fun onItemClick(product: UserPublicProfileResponse.Data.Product, position: Int)
    }
}
