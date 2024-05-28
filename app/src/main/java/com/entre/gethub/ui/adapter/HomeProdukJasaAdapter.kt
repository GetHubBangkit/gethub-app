package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.products.Product
import com.entre.gethub.databinding.ItemHomeKelolamygethubProdukjasaBinding

class HomeProdukJasaAdapter(
    private var produkjasaList: MutableList<Product>,
    private val listener: (Product, Int) -> Unit,
) :
    RecyclerView.Adapter<HomeProdukJasaAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeKelolamygethubProdukjasaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(produkjasaList[position])
        holder.itemView.setOnClickListener { listener(produkjasaList[position], position) }
    }

    override fun getItemCount(): Int {
        return produkjasaList.size
    }

    fun setProdukJasaList(produkjasaList: MutableList<Product>) {
        this.produkjasaList = produkjasaList
        notifyDataSetChanged()
    }

    fun removeProduk(id: String) {
        val index = produkjasaList.indexOfFirst { it.id.toString() == id }
        if (index != -1) {
            produkjasaList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class ViewHolder(
        private val itemProdukJasaBinding: ItemHomeKelolamygethubProdukjasaBinding,
        private val onItemClickCallback: OnItemClickCallback
    ) :
        RecyclerView.ViewHolder(itemProdukJasaBinding.root) {
        fun bindItem(produkjasa: Product) {
            with(itemProdukJasaBinding) {
                Glide.with(itemView)
                    .load(produkjasa.imageUrl)
                    .placeholder(R.drawable.ic_image)
                    .into(ivProductImage)
                tvProductTitle.text = produkjasa.name
                tvProductDesc.text = produkjasa.description
                ivDeleteProduct.setOnClickListener {
                    onItemClickCallback.onDeleteProductItem(produkjasa, adapterPosition)
                }
            }
        }
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        this.onItemClickCallback = callback
    }

    interface OnItemClickCallback {
        fun onDeleteProductItem(product: Product, position: Int)
    }
}