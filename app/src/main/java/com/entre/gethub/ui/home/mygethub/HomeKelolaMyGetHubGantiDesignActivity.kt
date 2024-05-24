package com.entre.gethub.ui.home.mygethub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityHomeKelolaMyGetHubGantiDesignBinding
import com.entre.gethub.ui.adapter.LayoutDesignAdapter
import com.entre.gethub.ui.models.LayoutDesign

class HomeKelolaMyGetHubGantiDesignActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeKelolaMyGetHubGantiDesignBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerViewHomeLayoutDesignGratis()
        setupRecyclerViewHomeLayoutDesignBayar()

        binding.iconBack.setOnClickListener {
            finish()
        }
        binding.btnSimpan.setOnClickListener {
            finish()
        }

    }


    private fun setupRecyclerViewHomeLayoutDesignGratis() {
        binding.recyclerViewHomeDesignGratis.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter =
                LayoutDesignAdapter(createLayoutDesignGratisList()) { layoutdesign, position ->
                    Toast.makeText(
                        this@HomeKelolaMyGetHubGantiDesignActivity,
                        "Clicked on actor: ${layoutdesign.image}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun createLayoutDesignGratisList(): ArrayList<LayoutDesign> {
        return arrayListOf(
            LayoutDesign(R.drawable.kelola_tema_designgratis),
            LayoutDesign(R.drawable.kelola_tema_designgratis),
            LayoutDesign(R.drawable.kelola_tema_designgratis)
        )
    }

    private fun setupRecyclerViewHomeLayoutDesignBayar() {
        binding.recyclerViewHomeDesignBayar.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = LayoutDesignAdapter(createLayoutDesignBayarList()) { layoutdesign, position ->
                Toast.makeText(
                    this@HomeKelolaMyGetHubGantiDesignActivity,
                    "Clicked on actor: ${layoutdesign.image}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createLayoutDesignBayarList(): ArrayList<LayoutDesign> {
        return arrayListOf(
            LayoutDesign(R.drawable.kelola_tema_designbayar),
            LayoutDesign(R.drawable.kelola_tema_designbayar),
            LayoutDesign(R.drawable.kelola_tema_designbayar)
        )
    }
}