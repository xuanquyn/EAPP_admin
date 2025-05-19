package com.example.eapp_admin.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.eapp_admin.R
import com.example.eapp_admin.view.chart.LineChartView
import com.example.eapp_admin.view.chart.LineChartView_Blue
import com.example.eapp_admin.viewmodel.UserViewModel
import com.github.mikephil.charting.data.Entry
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import kotlin.math.round
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eapp_admin.data.repository.TransactionRepository
import com.example.eapp_admin.data.repository.UserRepository
import com.example.eapp_admin.viewmodel.TransactionViewModel
import com.example.eapp_admin.viewmodel.TxViewModelFactory
import com.example.eapp_admin.viewmodel.UserViewModelFactory
import com.example.eapp_admin.viewmodel.VocabSetViewModel


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    vocabsetviewModel: VocabSetViewModel = viewModel()
) {
    val userRepository = remember { UserRepository() } // Hoặc cách lấy repository phù hợp với ứng dụng của bạn
    val factory = remember { UserViewModelFactory(userRepository) }
    val userViewModel: UserViewModel = viewModel(factory = factory)

    val repoTran = remember { TransactionRepository() }
    val transactionViewModel: TransactionViewModel = viewModel(factory = TxViewModelFactory(repoTran))

    val year = remember { mutableStateOf(2025) }

    val count_user by userViewModel.userCountNum.collectAsState()
    val count_premium by userViewModel.premiumUserCount.collectAsState()
    val count_vocabset by vocabsetviewModel.totalSets.collectAsState()

    // Tính doanh thu
    val rawRevenue = count_premium * 0.599
    val revenueRounded = remember(rawRevenue) {
        (round(rawRevenue * 10)) / 10.0
    }

    val entries_user by userViewModel.monthlyRegistrations.collectAsState()
    Log.d("UserViewModel2", "entries = $entries_user")

    val values_doanhthu by transactionViewModel.monthlyRevenue.collectAsState()
    Log.d("Transaction View Model", "entries = $values_doanhthu")

    // Theo dõi năm hiện tại từ ViewModel
    val userYear by userViewModel.currentYear.collectAsState()
    val revenueYear by transactionViewModel.currentYear.collectAsState()

    val repo = remember { UserRepository() }

    val viewModel: UserViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                UserViewModel(repo) as T
        }
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xfff2f1eb))
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .offset(x = 0.dp,
                    y = 19.dp)
                .requiredWidth(width = 375.dp)
                .requiredHeight(height = 40.dp)
        ) {
            Text(
                text = "Dashboard",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.18.em,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 139.dp,
                        y = 10.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 192.dp,
                    y = 76.dp)
                .requiredWidth(width = 160.dp)
                .requiredHeight(height = 83.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x=30.dp)
                    .requiredWidth(width = 170.dp)
                    .requiredHeight(height = 83.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(brush = Brush.linearGradient(
                        0.44f to Color.Black.copy(alpha = 0.96f),
                        1f to Color(0xff6c6b6b),
                        start = Offset(80f, 0f),
                        end = Offset(80f, 200f))))
            Text(
                text = "Premium",
                color = Color.White,
                lineHeight = 1.25.em,
                style = TextStyle(
                    fontSize = 20.sp,
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 48.63.dp,
                        y = 13.dp)
                    .requiredWidth(width = 100.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))
            Image(
                painter = painterResource(id = R.drawable.fluentpremium20regular),
                contentDescription = "fluent:premium-20-regular",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 143.51.dp,
                        y = 47.dp)
                    .requiredSize(size = 30.dp))
            Text(
                text = count_premium.toString(),
                color = Color.White,
                lineHeight = 0.71.em,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
//                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 48.63.dp,
                        y = 45.dp)
                    .requiredWidth(width = 60.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 30.dp,
                    y = 76.dp)
                .requiredWidth(width = 160.dp)
                .requiredHeight(height = 83.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 170.dp)
                    .requiredHeight(height = 83.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.linearGradient(
                            0.02f to Color(0xffffa768),
                            0.45f to Color(0xffdf6b18),
                            start = Offset(45.48f, 200f),
                            end = Offset(45.48f, 0f)
                        )
                    )
            )
            Text(
                text = "User",
                color = Color.White,
                lineHeight = 1.25.em,
                style = TextStyle(
                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 17.53.dp,
                            y = 13.dp)
                    .requiredWidth(width = 50.dp))
//                    .wrapContentHeight(align = Alignment.CenterVertically))
            Image(
                painter = painterResource(id = R.drawable.ionpeople),
                contentDescription = "ion:people",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 108.42.dp,
                        y = 45.dp)
                    .requiredSize(size = 30.dp))
            Text(
                text = count_user.toString(),
                color = Color.White,
                lineHeight = 0.71.em,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
//                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 17.53.dp,
                            y = 42.dp)
                    .requiredWidth(width = 60.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 191.dp,
                    y = 168.dp)
                .requiredWidth(width = 161.dp)
                .requiredHeight(height = 83.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x=30.dp)
                    .requiredWidth(width = 170.dp)
                    .requiredHeight(height = 83.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(brush = Brush.linearGradient(
                        0.02f to Color(0xFF84A2FA),
                        0.45f to Color(0xff4476f6),
                        start = Offset(45.76f, 200f),
                        end = Offset(45.76f, 0f))))
            Text(
                text = "Doanh thu",
                color = Color.White,
                lineHeight = 1.25.em,
                style = TextStyle(
                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 48.44.dp,
                        y = 14.dp)
                    .requiredWidth(width = 93.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 110.96.dp,
                        y = 42.35.dp)
                    .requiredSize(size = 27.dp)
            ) {
                Text(
                    text = "tr",
                    color = Color.White,
                    lineHeight = 0.83.em,
                    style = TextStyle(
                        fontSize = 30.sp
                    ),
//                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 35.dp,
                            y = 2.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
            Text(
                text = revenueRounded.toString(),
                color = Color.White,
                lineHeight = 0.71.em,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
//                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 48.44.dp,
                        y = 43.dp)
                    .requiredWidth(width = 60.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 30.dp,
                    y = 168.dp)
                .requiredWidth(width = 160.dp)
                .requiredHeight(height = 83.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 170.dp)
                    .requiredHeight(height = 83.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(brush = Brush.linearGradient(
                        0.48f to Color(0xFF1FAB34),
                        1f to Color(0xFF7AE08E),
                        start = Offset(80f, 0f),
                        end = Offset(80f, 200f))))
            Text(
                text = "Flashcard",
                color = Color.White,
                lineHeight = 1.25.em,
                style = TextStyle(
                    fontSize = 20.sp,
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 16.44.dp,
                        y = 14.dp)
                    .requiredWidth(width = 100.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))
            Image(
                painter = painterResource(id = R.drawable.materialsymbolsdictionary),
                contentDescription = "material-symbols:dictionary",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 111.32.dp,
                        y = 45.dp)
                    .requiredSize(size = 30.dp))
            Text(
                text = count_vocabset.toString(),
                color = Color.White,
                lineHeight = 0.71.em,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
//                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 16.44.dp,
                        y = 43.dp)
                    .requiredWidth(width = 60.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }

        //LineChart User
        Column(
            modifier = Modifier
                .padding(top = 270.dp, start = 27.dp, end = 27.dp, bottom = 30.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "User",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFFFA6F3E),
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp)
                    .align(Alignment.Start)

            )
            LineChartView(modifier = Modifier.fillMaxWidth().height(180.dp), values = entries_user)

            // Tạo hàng chứa 2 button và năm ở giữa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { userViewModel.setYear(userYear - 1) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp), // Không có padding thêm
                    elevation = null // Tắt bóng đổ
                )
                {
                    Text("<", color = Color.Black)
                }

                Text(text = userYear.toString(), modifier = Modifier.padding(0.dp))

                Button(
                    onClick = {userViewModel.setYear(userYear + 1)},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp), // Không có padding thêm
                    elevation = null // Tắt bóng đổ
                )
                {
                    Text(">", color = Color.Black)
                }
            }
        }

        //LineChart Doanh thu
        Column(
            modifier = Modifier
                .padding(top = 540.dp, start = 27.dp, end = 27.dp, bottom = 30.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "Doanh thu",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF4476F6),
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp)
                    .align(Alignment.Start)

            )
            LineChartView_Blue(modifier = Modifier.fillMaxWidth().height(180.dp), values = values_doanhthu)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { transactionViewModel.setYear(revenueYear - 1) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null
                )
                {
                    Text("<", color = Color.Black)
                }

                Text(text = revenueYear.toString(), modifier = Modifier.padding(0.dp))

                Button(
                    onClick = { transactionViewModel.setYear(revenueYear + 1) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp), // Không có padding thêm
                    elevation = null // Tắt bóng đổ
                )
                {
                    Text(">", color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen(Modifier)
    //    val values_user = listOf(
//        Entry(1f, 2f),
//        Entry(2f, 4f),
//        Entry(3f, 3f),
//        Entry(4f, 4.5f),
//        Entry(5f, 6f),
//        Entry(6f, 4.5f),
//        Entry(7f, 5f),
//        Entry(8f, 6f),
//        Entry(9f, 10f),
//        Entry(10f, 8f),
//        Entry(11f, 5f),
//        Entry(12f, 8f)
//    )

//    val values_doanhthu = listOf(
//        Entry(1f, 60f),
//        Entry(2f, 55f),
//        Entry(3f, 50f),
//        Entry(4f, 70f),
//        Entry(5f, 50f),
//        Entry(6f, 55f),
//        Entry(7f, 90f),
//        Entry(8f, 60f),
//        Entry(9f, 70f),
//        Entry(10f, 80f),
//        Entry(11f, 50f),
//        Entry(12f, 40f)
//    )
}