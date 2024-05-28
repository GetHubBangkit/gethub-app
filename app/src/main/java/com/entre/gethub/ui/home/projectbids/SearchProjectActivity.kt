package com.entre.gethub.ui.home.projectbids

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivitySearchProjectBinding

class SearchProjectActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchProjectBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            finish()
        }
    }
}