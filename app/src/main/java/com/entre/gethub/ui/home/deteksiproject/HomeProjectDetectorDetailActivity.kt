package com.entre.gethub.ui.home.deteksiproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.databinding.ActivityHomeProjectDetectorDetailBinding
import com.entre.gethub.ui.adapter.HomeProjectDetectorInsightAdapter
import com.entre.gethub.ui.adapter.HomeProjectDetectorPrediksiAdapter

class HomeProjectDetectorDetailActivity : AppCompatActivity() {

    private val binding: ActivityHomeProjectDetectorDetailBinding by lazy {
        ActivityHomeProjectDetectorDetailBinding.inflate(layoutInflater)
    }

    private lateinit var prediksiAdapter: HomeProjectDetectorPrediksiAdapter
    private lateinit var insightAdapter: HomeProjectDetectorInsightAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Dummy data for demonstration
        val prediksiList = listOf(
            ProjectDetectorResponse.Insight("Prediksi 1"),
            ProjectDetectorResponse.Insight("Prediksi 2"),
            ProjectDetectorResponse.Insight("Prediksi 3")
        )
        val insightList = listOf(
            ProjectDetectorResponse.Insight("Insight 1"),
            ProjectDetectorResponse.Insight("Insight 2"),
            ProjectDetectorResponse.Insight("Insight 3")
        )

        // Initialize adapters
        prediksiAdapter = HomeProjectDetectorPrediksiAdapter(prediksiList)
        insightAdapter = HomeProjectDetectorInsightAdapter(insightList)

        // Set layout manager and adapters for RecyclerViews
        binding.rvPrediksi.apply {
            layoutManager = LinearLayoutManager(this@HomeProjectDetectorDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = prediksiAdapter
        }

        binding.rvInsight.apply {
            layoutManager = LinearLayoutManager(this@HomeProjectDetectorDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = insightAdapter
        }
    }
}
