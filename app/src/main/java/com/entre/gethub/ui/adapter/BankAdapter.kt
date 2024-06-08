package com.entre.gethub.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.entre.gethub.data.remote.response.Category
import com.entre.gethub.data.remote.response.projects.AllBanksResponse

class BankAdapter(
    context: Context,
    private val banks: List<AllBanksResponse.DataItem>
) : ArrayAdapter<AllBanksResponse.DataItem>(context, android.R.layout.simple_spinner_item, banks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = banks[position].bankName
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = banks[position].bankName
        return view
    }

    fun getBankName(position: Int): String {
        return banks[position].bankName ?: ""
    }
}
