package com.entre.gethub.ui.analitic

import CustomMarker
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.CardViewersResponse
import com.entre.gethub.databinding.FragmentAnaliticBinding
import com.entre.gethub.ui.adapter.AnaliticGethubDilihatAdapter
import com.entre.gethub.utils.ViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

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
        setupLineChart() // Memanggil fungsi untuk mengatur grafik garis

        getAnaliticTotal()
        getCardViewers()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAnaliticTotal() {
        analiticViewModel.getAnaliticTotal().observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Empty -> {
                    // Handle empty result
                }
                is Result.Loading -> {
                    // Handle loading state
                }
                is Result.Success -> {
                    // Handle successful result
                    val analiticTotalResponse = result.data
                    // Bind the data to views
                    analiticTotalResponse.data?.let { data ->
                        binding.jumlahCardviewer.text = data.totalCardViewers.toString()
                        binding.jumlahWebviewer.text = data.totalWebViewers.toString()
                        binding.jumlahpartner.text = data.totalPartner.toString()
                    }
                }
                is Result.Error -> {
                    // Handle error
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other states
                }
            }
        })
    }

    private fun getCardViewers() {
        analiticViewModel.getCardViewers().observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Empty -> {
                    // Handle empty result
                }
                is Result.Loading -> {
                    // Handle loading state
                }
                is Result.Success -> {
                    // Handle successful result
                    val cardViewersResponse = result.data
                    // Update RecyclerView with data from API
                    cardViewersResponse.data?.let { dataItems ->
                        viewersList.clear()
                        viewersList.addAll(dataItems)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Result.Error -> {
                    // Handle error
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other states
                }
            }
        })
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
        vl.fillAlpha = 255

        binding.lineChart.xAxis.labelRotationAngle = 0f
        binding.lineChart.data = LineData(vl)
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.xAxis.axisMaximum = 7f

        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.setPinchZoom(true)
        binding.lineChart.description.text = "Days"
        binding.lineChart.setNoDataText("Tidak Ada Data")
        binding.lineChart.animateX(1800, Easing.EaseInExpo)

        val markerView = CustomMarker(requireContext(), R.layout.marker_view)
        binding.lineChart.marker = markerView
    }
}
