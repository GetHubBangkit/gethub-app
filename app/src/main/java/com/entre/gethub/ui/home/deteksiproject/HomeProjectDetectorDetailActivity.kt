package com.entre.gethub.ui.home.deteksiproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        Log.d("HomeProjectDetailActivity", "result: $result ")


        result?.data?.results?.let { showPredictions(it) }
        result?.data?.insight?.let { showInsight(it) }
    }

    private fun showPredictions(results: List<ProjectDetectorResponse.Result>) {
       binding.rvPrediksi.apply {
           layoutManager= LinearLayoutManager(this@HomeProjectDetectorDetailActivity, LinearLayoutManager.VERTICAL, false)
           adapter=HomeProjectDetectorPrediksiAdapter(results)
       }
    }

    private fun showInsight(insight: List<ProjectDetectorResponse.Insight>) {
        binding.rvInsight.apply {
            layoutManager= LinearLayoutManager(this@HomeProjectDetectorDetailActivity, LinearLayoutManager.VERTICAL, false)
            adapter=HomeProjectDetectorInsightAdapter(insight)
        }
    }
}
