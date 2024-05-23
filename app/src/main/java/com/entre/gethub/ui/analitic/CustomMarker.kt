import android.content.Context
import android.widget.TextView
import com.entre.gethub.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


class CustomMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    // TextView yang akan menampilkan nilai
    private val tvPrice: TextView = findViewById(R.id.tvPrice)

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        if (entry != null) {
            val value = entry.y.toInt()
            tvPrice.text = "$value Views"
        }
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}