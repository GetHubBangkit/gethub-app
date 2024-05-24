package com.entre.gethub.ui.home.projectbids

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.databinding.ItemDetailProjectbidsFormBinding

class HomeDetailProjectBidsFormActivity : AppCompatActivity() {

    private val binding by lazy { ItemDetailProjectbidsFormBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val rekrutprice = intent.getStringExtra("rekrutprice")

        rekrutprice?.let {
            binding.rekrutprice.text = it
        }

        binding.iconBack.setOnClickListener {
            finish()
        }
    }
}