package com.entre.gethub.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.databinding.ItemHomeProjectDetectorPrediksiBinding

class HomeProjectDetectorPrediksiAdapter(
    private val prediksiList: List<ProjectDetectorResponse.Result>
) : RecyclerView.Adapter<HomeProjectDetectorPrediksiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeProjectDetectorPrediksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = prediksiList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = prediksiList.size

    class ViewHolder(private val binding: ItemHomeProjectDetectorPrediksiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ProjectDetectorResponse.Result) {
            binding.tvPrediksiDesc.text = result.text
            binding.tvPersenFraudCheck.text = "${(result.accuracy?.times(100))?.toFixed(1)}%"
            binding.cvFraud.apply {
                backgroundTintList = ColorStateList.valueOf(
                    when (result.prediction) {
                        "real_project_job" -> ContextCompat.getColor(context, R .color.green) // Green color
                        "fraud_project_job" -> ContextCompat.getColor(context, R.color.red) // Red color
                        else -> ContextCompat.getColor(context, R.color.green) // Default color
                    }
                )
            }
            binding.cvSentiment.apply {
                backgroundTintList = ColorStateList.valueOf(
                    when (result.sentiment) {
                        "Netral" -> ContextCompat.getColor(context, R.color.darkblue_netral)
                        "Positif" -> ContextCompat.getColor(context, R.color.green)
                        "Negatif" -> ContextCompat.getColor(context, R.color.red)
                        else -> ContextCompat.getColor(context, R.color.green)
                    }
                )
            }


            binding.tvDetailFraud.text = when (result.prediction) {
                "real_project_job" -> "Real"
                "fraud_project_job" -> "Fraud"
                else -> result.prediction
            }
            binding.tvPersenSentiment.text = "${result.sentimentAccuracy?.toFixed(1)}%"
            binding.tvDetailSentiment.text = result.sentiment
        }
    }
}

private fun Double?.toFixed(digits: Int): String? {
    return this?.let {
        "%.${digits}f".format(it)
    }
}
