package com.entre.gethub.ui.home.mygethub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.databinding.ActivityHomeKelolaMyGetHubGantiDesignBinding
import com.entre.gethub.di.Injection
import com.entre.gethub.ui.adapter.LayoutDesignAdapter
import com.entre.gethub.ui.models.LayoutDesign
import com.entre.gethub.utils.ViewModelFactory

class HomeKelolaMyGethubGantiDesignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeKelolaMyGetHubGantiDesignBinding
    private lateinit var viewModel: HomeKelolaMyGethubGantiDesignViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeKelolaMyGetHubGantiDesignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeKelolaMyGethubGantiDesignViewModel::class.java)

        setupViews()

        binding.iconBack.setOnClickListener {
            finish()
        }
        binding.btnPriview.setOnClickListener {

        }
    }

    private fun setupViews() {
        setupRecyclerViewHomeLayoutDesignGratis()
        setupRecyclerViewHomeLayoutDesignBayar()
    }

    private fun setupRecyclerViewHomeLayoutDesignGratis() {
        binding.recyclerViewHomeDesignGratis.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = LayoutDesignAdapter(createLayoutDesignGratisList()) { layoutdesign, position ->
                Toast.makeText(
                    this@HomeKelolaMyGethubGantiDesignActivity,
                    "Clicked on actor: ${layoutdesign.image}",
                    Toast.LENGTH_SHORT
                ).show()
                // Call updateThemeHub with the appropriate theme hub value
                viewModel.updateThemeHub(position + 1)
            }
        }
    }

    private fun createLayoutDesignGratisList(): ArrayList<LayoutDesign> {
        return arrayListOf(
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card1/card1.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card2/card2.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card3/card3.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card4/card4.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card5/card5.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card6/card6.png")
        )
    }

    private fun setupRecyclerViewHomeLayoutDesignBayar() {
        binding.recyclerViewHomeDesignBayar.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = LayoutDesignAdapter(createLayoutDesignBayarList()) { layoutdesign, position ->
                Toast.makeText(
                    this@HomeKelolaMyGethubGantiDesignActivity,
                    "Clicked on actor: ${layoutdesign.image}",
                    Toast.LENGTH_SHORT
                ).show()
                // Call updateThemeHub with the appropriate theme hub value
                viewModel.updateThemeHub(position + 7)
            }
        }
    }

    private fun createLayoutDesignBayarList(): ArrayList<LayoutDesign> {
        return arrayListOf(
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card7/card7.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card8/card8.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card9/card9.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card10/card.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card11/card.png"),
            LayoutDesign("https://storage.googleapis.com/gethub_bucket/CARD/card12/card12.png")
        )
    }
}
