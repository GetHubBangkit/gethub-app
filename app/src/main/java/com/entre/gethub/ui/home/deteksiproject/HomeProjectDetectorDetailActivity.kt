package com.entre.gethub.ui.home.deteksiproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.databinding.ActivityHomeProjectDetectorDetailBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.adapter.HomeProjectDetectorInsightAdapter
import com.entre.gethub.ui.adapter.HomeProjectDetectorPrediksiAdapter

class HomeProjectDetectorDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeProjectDetectorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeProjectDetectorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeProjectDetectorActivity::class.java))
        }

        val imageUriString = intent.getStringExtra("imageUri")
        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        val result = intent.getParcelableExtra<ProjectDetectorResponse>("fraudDetectionResult")

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.ivDeteksi.setImageURI(imageUri)
        } else if (imageBitmap != null) {
            binding.ivDeteksi.setImageBitmap(imageBitmap)
        }

        result?.results?.let { showPredictions(it) }
        result?.insight?.let { showInsight(it) }
    }

    private fun showPredictions(results: List<ProjectDetectorResponse.Result>) {
        val adapter = HomeProjectDetectorPrediksiAdapter(results)
        binding.rvPrediksi.adapter = adapter
    }

    private fun showInsight(insight: List<ProjectDetectorResponse.Insight>) {
        val adapter = HomeProjectDetectorInsightAdapter(insight)
        binding.rvInsight.adapter = adapter
    }
}
