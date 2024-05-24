package com.entre.gethub.ui.gethub

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityGethubAddPartnerBinding

class GethubAddPartnerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGethubAddPartnerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Menambahkan onClickListener pada ImageView manual
        binding.ivManual.setOnClickListener {
            // Intent untuk berpindah ke CompleteProfileActivity
            val intent = Intent(this, GethubPartnerFormActivity::class.java)
            startActivity(intent)
        }

        binding.iconBack.setOnClickListener {
            finish()
        }

    }
}