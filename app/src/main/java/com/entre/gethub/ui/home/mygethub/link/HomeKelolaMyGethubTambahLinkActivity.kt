package com.entre.gethub.ui.home.mygethub.link

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityHomeKelolaMyGethubTambahLinkBinding
import com.entre.gethub.ui.home.HomeViewModel
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubViewModel
import com.entre.gethub.utils.ViewModelFactory

class HomeKelolaMyGethubTambahLinkActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeKelolaMyGethubTambahLinkBinding.inflate(layoutInflater) }
    private val homeKelolaMyGethubViewModel: HomeKelolaMyGethubViewModel by viewModels {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val categories = listOf("Shopee", "Tiktok")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = adapter

        binding.iconBack.setOnClickListener {
            finish()
        }

        binding.btnSimpan.setOnClickListener {
            val category = binding.spinnerKategori.selectedItem.toString()
            val link = binding.etLink.text.toString()
            homeKelolaMyGethubViewModel.addLink(category, link)
            finish()
        }
    }

}