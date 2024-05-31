package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.databinding.ItemHomeProjectDetectorPrediksiBinding
import java.text.DecimalFormat

class HomeProjectDetectorPrediksiAdapter(
    private val dataList: List<ProjectDetectorResponse.Insight>
) : RecyclerView.Adapter<HomeProjectDetectorPrediksiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemHomeProjectDetectorPrediksiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: ItemHomeProjectDetectorPrediksiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val decimalFormat = DecimalFormat("#.#")

        fun bind(item: ProjectDetectorResponse.Insight) {
            binding.tvPrediksiDesc.text = item.text
        }

        private fun showPrediction(prediction: String?) {
            prediction?.let {
                val resultText = if (it == "real_project_job") "Real" else "Fraud"
                binding.tvDetailFraud.text = resultText
            }
        }

        private fun showAccuracy(accuracy: Double?) {
            accuracy?.let {
                val formattedAccuracy = decimalFormat.format(it * 100)
                binding.tvPersenFraudCheck.text = "$formattedAccuracy%"
            }
        }

        private fun showSentiment(sentimentAccuracy: Double?, sentiment: String?) {
            sentimentAccuracy?.let {
                val formattedSentimentAccuracy = decimalFormat.format(it)
                binding.tvPersenSentiment.text = "$formattedSentimentAccuracy%"
            }

            sentiment?.let {
                binding.tvDetailSentiment.text = it
            }
        }
    }
}
