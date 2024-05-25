package com.entre.gethub.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.entre.gethub.data.remote.response.Category

class CategoryAdapter(
    context: Context,
    private val categories: List<Category>
) : ArrayAdapter<Category>(context, android.R.layout.simple_spinner_item, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = categories[position].name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = categories[position].name
        return view
    }

    fun getCategoryId(position: Int): String {
        return categories[position].id ?: ""
    }
}
