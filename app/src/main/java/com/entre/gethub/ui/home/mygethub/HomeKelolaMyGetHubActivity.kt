package com.entre.gethub.ui.home.mygethub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityHomeKelolaMyGetHubBinding

class HomeKelolaMyGetHubActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeKelolaMyGetHubBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}