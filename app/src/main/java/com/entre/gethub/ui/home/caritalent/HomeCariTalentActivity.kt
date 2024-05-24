package com.entre.gethub.ui.home.caritalent

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityHomeCariTalentBinding
import com.entre.gethub.ui.adapter.CariTalentAdapter

class HomeCariTalentActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeCariTalentBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerViewCariTalent()

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerViewCariTalent() {
        binding.rvHomeCariTalent.apply {
            layoutManager = LinearLayoutManager(this@HomeCariTalentActivity)
            adapter = CariTalentAdapter(createCariTalentList()) { caritalent, position ->
                Toast.makeText(
                    this@HomeCariTalentActivity,
                    "Clicked on actor: ${caritalent.profilename}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createCariTalentList(): ArrayList<CariTalent> {
        return arrayListOf(
            CariTalent("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            CariTalent("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            CariTalent("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            CariTalent("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            CariTalent("Budi Santoso", R.drawable.profilepic1, "Software Engineer")
        )
    }
}

data class CariTalent (var profilename: String, var profilepic: Int, var profiledesc: String)