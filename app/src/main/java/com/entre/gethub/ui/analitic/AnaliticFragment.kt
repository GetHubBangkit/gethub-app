package com.entre.gethub.ui.analitic

import com.entre.gethub.ui.analitic.CustomMarker
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.CardViewersResponse
import com.entre.gethub.data.remote.response.GraphDataResponse
import com.entre.gethub.databinding.FragmentAnaliticBinding
import com.entre.gethub.ui.adapter.AnaliticGethubDilihatAdapter
import com.entre.gethub.utils.ViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AnaliticFragment : Fragment() {

    private val analiticViewModel by viewModels<AnaliticViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentAnaliticBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AnaliticGethubDilihatAdapter
    private val viewersList = mutableListOf<CardViewersResponse.DataItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnaliticBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerViewAnaliticGethubDilihat()
        setupLineChart()

        getAnaliticTotal()
        getCardViewers()
        getGraphData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAnaliticTotal() {
        analiticViewModel.getAnaliticTotal().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val analiticTotalResponse = result.data
                    analiticTotalResponse.data?.let { data ->
                        binding.jumlahCardviewer.text = data.totalCardViewers.toString()
                        binding.jumlahWebviewer.text = data.totalWebViewers.toString()
                        binding.jumlahpartner.text = data.totalPartner.toString()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun getCardViewers() {
        analiticViewModel.getCardViewers().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val cardViewersResponse = result.data
                    cardViewersResponse.data?.let { dataItems ->
                        viewersList.clear()
                        viewersList.addAll(dataItems)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun getGraphData() {
        analiticViewModel.getGraphData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val graphDataResponse = result.data
                    val entries = ArrayList<Entry>()
                    val dates = ArrayList<String>() // Menyimpan tanggal

                    graphDataResponse.data?.forEachIndexed { index, data ->
                        entries.add(Entry(index.toFloat() + 1, data.totalViews.toFloat()))
                        dates.add(data.date) // Menambahkan tanggal ke dalam list
                    }

                    val vl = LineDataSet(entries, "Views")
                    vl.setDrawValues(false)
                    vl.setDrawFilled(true)
                    vl.lineWidth = 3f
                    vl.fillColor = ContextCompat.getColor(requireContext(), R.color.teal)
                    vl.fillAlpha = 255

                    binding.lineChart.xAxis.labelRotationAngle = 0f
                    binding.lineChart.data = LineData(vl)
                    binding.lineChart.axisRight.isEnabled = false
                    binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(dates) // Mengatur formatter dengan tanggal
                    binding.lineChart.xAxis.position = XAxis.XAxisPosition.TOP

                    // Mengatur sumbu Y agar menampilkan angka bulat saja
                    binding.lineChart.axisLeft.valueFormatter = IntValueFormatter()

                    binding.lineChart.setTouchEnabled(true)
                    binding.lineChart.setPinchZoom(true)
                    binding.lineChart.description.text = "Days"
                    binding.lineChart.setNoDataText("Tidak Ada Data")
                    binding.lineChart.animateX(1800, Easing.EaseInExpo)

                    val markerView = CustomMarker(requireContext(), R.layout.marker_view, dates)
                    binding.lineChart.marker = markerView
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }


    private fun setupRecyclerViewAnaliticGethubDilihat() {
        adapter = AnaliticGethubDilihatAdapter(viewersList) { viewer, position ->
            Toast.makeText(
                this@AnaliticFragment.requireContext(),
                "Clicked on viewer: ${viewer.fullName}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.recyclerViewGethubDilihat.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@AnaliticFragment.adapter
        }
    }

    private fun setupLineChart() {
        // Setup line chart
    }
}
