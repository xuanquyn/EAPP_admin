package com.example.eapp_admin.view.chart

import android.graphics.Color
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.graphics.drawable.GradientDrawable
import androidx.compose.runtime.remember
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun LineChartView_Blue(
    modifier: Modifier = Modifier,
    values: List<Entry>
) {
    val oneDecimalFormatter = remember {
        object : ValueFormatter() {
            override fun getFormattedValue(value: Float) =
                String.format("%.1f", value)
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            // Tạo LineChart trong factory block (chỉ chạy một lần khi tạo view)
            val lineChart = LineChart(context)

            // Cấu hình cơ bản cho chart (những thứ không đổi)
            lineChart.apply {
                // Di chuyển trục X xuống dưới và hiển thị tất cả các giá trị từ 1 đến 12
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setLabelCount(12, true)

                // Bỏ các đường kẻ ngang
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)

                // Ẩn Legend và Description
                legend.isEnabled = false
                description.isEnabled = false

                // Thiết lập khác nếu cần
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
            }

            lineChart
        },
        update = { lineChart ->
            // Block này chạy mỗi khi có recomposition và dữ liệu thay đổi

            // Tạo dataset với dữ liệu mới
            val dataSet = LineDataSet(values, "Label").apply {
                color = Color.parseColor("#4476F6")

                // 2. Chỉnh nút dữ liệu thành hình tròn có viền ngoài dày màu trắng, bên trong màu "FA6F3E"
                setDrawCircles(true)  // Bật nút hình tròn
                setCircleColor(Color.parseColor("#4476F6")) // Màu trong nút
                circleRadius = 4f  // Kích thước nút
                setCircleHoleColor(Color.WHITE) // Màu viền nút
                circleHoleRadius = 2f  // Kích thước viền nút

                valueTextSize = 11f
                valueFormatter = oneDecimalFormatter

                // 3. Đảm bảo đường line phải cong ở đỉnh, không gấp khúc
                mode = LineDataSet.Mode.CUBIC_BEZIER // Để tạo đường cong mượt mà thay vì gấp khúc

                // Thiết lập fill dưới đường line
                setDrawFilled(true)
                val drawable = GradientDrawable()
                drawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                drawable.colors = intArrayOf(Color.parseColor("#CAE4FD"), Color.parseColor("#FFFFFF"))
                fillDrawable = drawable // Gán Drawable vào fillDrawable của LineDataSet
            }

            // Tạo LineData mới
            val lineData = LineData(dataSet)

            // Cập nhật dữ liệu cho chart
            lineChart.data = lineData

            // ĐIỀU QUAN TRỌNG: Thông báo chart cập nhật
            lineChart.invalidate()

        }
    )
}

@Preview(showBackground = true)
@Composable
fun LineChartView_BluePreview() {
    // Dữ liệu tượng trưng cho biểu đồ (tạo các điểm dữ liệu giả lập)
    val values = listOf(
        Entry(1f, 2f),
        Entry(2f, 4f),
        Entry(3f, 3f),
        Entry(4f, 4.5f),
        Entry(5f, 6f),
        Entry(6f, 4.5f),
        Entry(7f, 5f),
        Entry(8f, 6f),
        Entry(9f, 10f),
        Entry(10f, 8f),
        Entry(11f, 5f),
        Entry(12f, 8f)
    )

    // Gọi LineChartView với dữ liệu tượng trưng
    LineChartView_Blue(modifier = Modifier.size(380.dp, 270.dp), values = values)
}