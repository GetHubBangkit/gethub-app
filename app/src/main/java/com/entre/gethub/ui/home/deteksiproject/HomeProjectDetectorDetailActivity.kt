package com.entre.gethub.ui.home.deteksiproject

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.databinding.ActivityHomeProjectDetectorDetailBinding
import com.entre.gethub.ui.adapter.HomeProjectDetectorInsightAdapter
import com.entre.gethub.ui.adapter.HomeProjectDetectorPrediksiAdapter

class HomeProjectDetectorDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeProjectDetectorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeProjectDetectorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menerima data gambar dari intent
        val imageUriString = intent.getStringExtra("imageUri")
        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")

        // Menampilkan gambar dari URI atau bitmap ke ImageView
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.ivDeteksi.setImageURI(imageUri)
        } else if (imageBitmap != null) {
            binding.ivDeteksi.setImageBitmap(imageBitmap)
        }

        // Menerima data hasil deteksi dari intent
        val result = intent.getParcelableExtra<ProjectDetectorResponse>("fraudDetectionResult")

        // Menampilkan prediksi pada RecyclerView
        result?.results?.let { showPredictions(it) }

        // Menampilkan insight pada RecyclerView
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
