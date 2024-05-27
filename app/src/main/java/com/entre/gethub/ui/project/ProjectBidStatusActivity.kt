package com.entre.gethub.ui.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityProjectBidStatusBinding

class ProjectBidStatusActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProjectBidStatusBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            finish()
        }
    }
}