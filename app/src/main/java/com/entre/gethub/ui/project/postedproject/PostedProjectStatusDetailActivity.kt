package com.entre.gethub.ui.project.postedproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityPostedProjectStatusBinding
import com.entre.gethub.databinding.ActivityPostedProjectStatusDetailBinding

class PostedProjectStatusDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostedProjectStatusDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}