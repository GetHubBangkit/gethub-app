package com.entre.gethub.ui.analitic

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.ValueFormatter

class IntValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return value.toInt().toString()
    }
}