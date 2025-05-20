package com.example.eapp_admin.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eapp_admin.R
import com.example.eapp_admin.data.model.TransactionUI
import com.example.eapp_admin.data.repository.TransactionRepository
import com.example.eapp_admin.data.repository.UserRepository
import com.example.eapp_admin.view.chart.LineChartView_Blue
import com.example.eapp_admin.view.manage_revenue.TransactionRow
import com.example.eapp_admin.viewmodel.TransactionViewModel
import com.example.eapp_admin.viewmodel.UserViewModel
import com.example.eapp_admin.viewmodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.eapp_admin.utils.captureToPdf
import com.github.mikephil.charting.data.Entry
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ManageRevenueScreen(
    modifier: Modifier = Modifier,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    ) {
    val userRepository = remember { UserRepository() }
    val factory = remember { UserViewModelFactory(userRepository) }
    val userViewModel: UserViewModel = viewModel(factory = factory)

    val repo = remember { TransactionRepository() }
    val vm: TransactionViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(c: Class<T>): T =
            TransactionViewModel(repo) as T
    })

    val userYear by userViewModel.currentYear.collectAsState()
    //Tính tổng doanh thu
    val count_premium by userViewModel.premiumUserCount.collectAsState()
    val rawRevenue = count_premium * 599000
    val formattedRevenue = remember(rawRevenue) {
        NumberFormat.getNumberInstance(Locale.US).apply {
            isGroupingUsed = true
        }.format(rawRevenue)
    }
    val values_doanhthu by vm.monthlyRevenue.collectAsState()
    val list by vm.items.collectAsState()
    val revenueYear by vm.currentYear.collectAsState()

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val contentView = LocalView.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xfff2f1eb))
    ) {
        Row(
            modifier = Modifier
                .offset(x = 0.dp,
                    y = 23.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.Center, // Các phần tử con mặc định căn giữa
            verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
        ) {
            Text(
                text = "Quản lý doanh thu",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.18.em,
                style = TextStyle(
                    fontSize = 17.sp ,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .offset(x = 110.dp,y = 0.dp)

            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.asset_down),
                contentDescription = "material-symbols:download-rounded",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier
                    .requiredSize(size = 24.dp)
                    .clickable {
                        // Bắt đầu quá trình xuất PDF trên một coroutine khác
                        //CoroutineScope(Dispatchers.Main).launch {
                        //    captureToPdf(activity, contentView, requestPermissionLauncher)
                        //}
                        //Here
                        CoroutineScope(Dispatchers.Main).launch {
                            captureToPdf(
                                activity       = activity,
                                composable     = {
                                    RevenueContent(
                                        revenueYear     = revenueYear,
                                        rawRevenue      = rawRevenue,
                                        valuesDoanhthu  = values_doanhthu,
                                        list            = list
                                    )
                                },
                                requestPermissionLauncher = requestPermissionLauncher
                            )
                        }
                        //Here
                    }
            )

        }

        Box(
            modifier = Modifier
//                .padding(top = 60.dp, start = 27.dp, end = 27.dp, bottom = 30.dp)
                .graphicsLayer()
        ) {
            //LineChart Doanh thu
            Column(
                modifier = Modifier
                    .padding(top = 60.dp, start = 27.dp, end = 27.dp, bottom = 30.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tổng doanh thu",
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                    Text(
                        text = formattedRevenue,
                        style = TextStyle(
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFFFA6F3E),
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp)
                    )
                    Text(
                        text = "VND",
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp)
                    )
                }

                LineChartView_Blue(
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    values = values_doanhthu
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { vm.setYear(revenueYear - 1) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        elevation = null
                    )
                    {
                        Text("<", color = Color.Black)
                    }

                    Text(text = revenueYear.toString(), modifier = Modifier.padding(0.dp))

                    Button(
                        onClick = { vm.setYear(revenueYear + 1) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp), // Không có padding thêm
                        elevation = null // Tắt bóng đổ
                    )
                    {
                        Text(">", color = Color.Black)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 410.dp, start = 27.dp, end = 27.dp, bottom = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Danh sách giao dịch",
                    color = Color.Black,
                    lineHeight = 1.43.em,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list, key = { it.id }) { tx ->
                        TransactionRow(tx, Modifier.align(Alignment.CenterHorizontally))
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 375, heightDp = 812)
@Composable
private fun ManageRevenuePreview() {
//    ManageRevenueScreen(Modifier)
}

@Composable
fun RevenueContent(
    revenueYear: Int,
    rawRevenue: Int,
    valuesDoanhthu: List<Entry>,
    list: List<TransactionUI>,
    onPrev: () -> Unit = {},
    onNext: () -> Unit = {},
)
{
    Box(
        modifier = Modifier
//                .padding(top = 60.dp, start = 27.dp, end = 27.dp, bottom = 30.dp)
            .graphicsLayer()
    ) {
        //LineChart Doanh thu
        Column(
            modifier = Modifier
                .padding(top = 60.dp, start = 27.dp, end = 27.dp, bottom = 30.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng doanh thu",
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                Text(
                    text = rawRevenue.toString(),
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFFA6F3E),
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                )
                Text(
                    text = "VND",
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                )
            }

            LineChartView_Blue(
                modifier = Modifier.fillMaxWidth().height(260.dp),
                values = valuesDoanhthu
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {onPrev},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null
                )
                {
                    Text("<", color = Color.Black)
                }

                Text(text = revenueYear.toString(), modifier = Modifier.padding(0.dp))

                Button(
                    onClick = { onNext},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp), // Không có padding thêm
                    elevation = null // Tắt bóng đổ
                )
                {
                    Text(">", color = Color.Black)
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 410.dp, start = 27.dp, end = 27.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Danh sách giao dịch",
                color = Color.Black,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list, key = { it.id }) { tx ->
                    TransactionRow(tx, Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}