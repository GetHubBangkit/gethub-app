package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.premium.PaymentHistoryResponse
import com.entre.gethub.databinding.ItemPaymentHistoryBinding
import com.entre.gethub.utils.Formatter

class PaymentHistoryAdapter(private val paymentHistoryList: List<PaymentHistoryResponse.DataItem>) :
    RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemPaymentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = paymentHistoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(paymentHistoryList[position])
    }

    class ViewHolder(private val binding: ItemPaymentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(paymentHistory: PaymentHistoryResponse.DataItem) {
            with(binding) {
                tvInvoiceNumber.text = paymentHistory.id
                tvInvoiceDate.text = "Tanggal Dibuat: ${paymentHistory.transactionDate}"
                tvInvoiceStatus.text = "Status: ${paymentHistory.status}"
                tvInvoiceAmount.text = "Total: ${
                    Formatter.formatRupiah(paymentHistory.amount ?: 0)
                }"
            }
        }
    }
}