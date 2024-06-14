package com.entre.gethub.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.data.remote.response.InformationHubResponse
import com.entre.gethub.databinding.ItemDetailInformationhubBinding
import com.bumptech.glide.Glide
import com.entre.gethub.data.remote.response.ReysEventResponse

class HomeDetailInformationHubHomeActivity : AppCompatActivity() {

    private lateinit var binding: ItemDetailInformationhubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailInformationhubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val informationHubList = intent.getParcelableArrayListExtra<ReysEventResponse.EventData>("information_hub")

        // Ambil objek pertama dari list
        val informationHub = informationHubList?.get(0)

        informationHub?.let {
            binding.tvtitle.text = it.title
            binding.tvInputKategori.text = it.category
            binding.tvdesc.text = it.description
            Glide.with(this).load(it.imageUrl).into(binding.ivimage)
        }

        binding.iconBack.setOnClickListener {
            onBackPressed()
        }
    }

}
