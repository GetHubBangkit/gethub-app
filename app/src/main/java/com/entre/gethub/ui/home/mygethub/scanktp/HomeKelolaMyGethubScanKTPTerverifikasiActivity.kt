package com.entre.gethub.ui.home.mygethub.scanktp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.entre.gethub.databinding.ActivityHomeKelolamygethubSudahverifikasiktpBinding
import com.entre.gethub.databinding.ActivityHomeKelolamygethubUploadverifikasiktpBinding
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity

class HomeKelolaMyGethubScanKTPTerverifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeKelolamygethubSudahverifikasiktpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeKelolamygethubSudahverifikasiktpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.iconBack.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubActivity::class.java))
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeKelolaMyGethubActivity::class.java))
        finish()
    }
}
