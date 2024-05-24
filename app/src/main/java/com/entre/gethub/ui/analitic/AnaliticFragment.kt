package com.entre.gethub.ui.analitic

import CustomMarker
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.ui.models.AnaliticGetHubDilihat
import com.entre.gethub.R

import com.entre.gethub.databinding.FragmentAnaliticBinding
import com.entre.gethub.ui.adapter.AnaliticGethubDilihatAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AnaliticFragment : Fragment() {

    private var _binding: FragmentAnaliticBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val akunViewModel = ViewModelProvider(this).get(AnaliticViewModel::class.java)
        _binding = FragmentAnaliticBinding.inflate(inflater, container, false)
        val root: View = binding.root

        akunViewModel.text.observe(viewLifecycleOwner) {
            // textView.text = it
        }
        setupRecyclerViewAnaliticGethubDilihat()

        setupLineChart() // Memanggil fungsi untuk mengatur grafik garis

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerViewAnaliticGethubDilihat() {
        binding.recyclerViewGethubDilihat.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = AnaliticGethubDilihatAdapter(createAnaliticGetHubDilihatList()) { analiticgethubdilihat, position ->
                Toast.makeText(
                    this@AnaliticFragment.requireContext(), // Gunakan requireContext() untuk mendapatkan Context yang benar
                    "Clicked on actor: ${analiticgethubdilihat.profilename}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createAnaliticGetHubDilihatList(): ArrayList<AnaliticGetHubDilihat> {
        return arrayListOf<AnaliticGetHubDilihat>(
            AnaliticGetHubDilihat(
                "Budi Santoso",
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            AnaliticGetHubDilihat(
                "Budi Santoso",
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            AnaliticGetHubDilihat(
                "Budi Santoso",
                R.drawable.profilepic1,
                "Software Enginer"
            )
        )
    }
    private fun setupLineChart() {
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))
        entries.add(Entry(6f, 5f))
        entries.add(Entry(7f, 3f))




        val vl = LineDataSet(entries, "Views")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = ContextCompat.getColor(requireContext(), R.color.teal)
        vl.fillAlpha = 255 // Mengatur alpha ke 255 (tidak memerlukan resource color)

        binding.lineChart.xAxis.labelRotationAngle = 0f
        binding.lineChart.data = LineData(vl)
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.xAxis.axisMaximum = 7f // Mengatur maksimum sumbu x ke 8

        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.setPinchZoom(true)
        binding.lineChart.description.text = "Days"
        binding.lineChart.setNoDataText("Tidak Ada Data")
        binding.lineChart.animateX(1800, Easing.EaseInExpo)

        val markerView = CustomMarker(requireContext(), R.layout.marker_view)
        binding.lineChart.marker = markerView
    }

}
