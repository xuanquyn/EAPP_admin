package com.example.eapp_admin.view.manage_user

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.eapp_admin.R
import com.example.eapp_admin.data.model.User
import com.example.eapp_admin.view.chart.LineChartView
import com.example.eapp_admin.view.chart.LineChartView_Blue
import com.example.eapp_admin.viewmodel.UserViewModel
import com.example.eapp_admin.viewmodel.VocabSetViewModel
import com.github.mikephil.charting.data.Entry
import com.example.eapp_admin.data.repository.UserRepository
import com.example.eapp_admin.viewmodel.UserViewModelFactory


@Composable
fun ManageUser1Screen(
    modifier: Modifier = Modifier,
//    userViewModel: UserViewModel = viewModel(),
    vocabsetviewModel: VocabSetViewModel = viewModel()
) {
    val userRepository = remember { UserRepository() } // Hoặc cách lấy repository phù hợp với ứng dụng của bạn
    val factory = remember { UserViewModelFactory(userRepository) }
    val userViewModel: UserViewModel = viewModel(factory = factory)

    val values_user by userViewModel.monthlyRegistrations.collectAsState()
    val userYear by userViewModel.currentYear.collectAsState()

//    val year = remember { mutableStateOf(2025) }
    val count_user by userViewModel.userCountNum.collectAsState()

    /* ---- tạo Repo & ViewModel (nếu chưa dùng Hilt) ---- */
    val repo       = remember { UserRepository() }
    val viewModel: UserViewModel = viewModel(factory = UserViewModelFactory(repo))

    /* ---- state ---- */
    val users   by viewModel.users.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error   by viewModel.error.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xfff2f1eb))
            .padding(top = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .offset(x = 0.dp,
                    y = 23.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quản lý người dùng",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.18.em,
                style = TextStyle(
                    fontSize = 17.sp ,
                    fontWeight = FontWeight.Bold
                ),

            )
        }

        //LineChart Doanh thu
        Column(
            modifier = Modifier
                .padding(top = 40.dp, start = 27.dp, end = 27.dp, bottom = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            LineChartView(modifier = Modifier.fillMaxWidth().height(300.dp), values = values_user)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { userViewModel.setYear(userYear - 1) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null
                )
                {
                    Text("<", color = Color.Black)
                }

                Text(text =  userYear.toString(), modifier = Modifier.padding(0.dp))

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

        Column(
            modifier = Modifier
                .padding(start = 28.dp, end = 20.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Column{
                Text(
                    text = "Danh sách người dùng",
                    color = Color.Black,
                    lineHeight = 1.43.em,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = count_user.toString(),
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.asset_people),
                        contentDescription = "image",
                        modifier = Modifier
                            .requiredSize(size = 24.dp)
                            .padding(start=5.dp)
                    )
                }
            }

//            Column {
//                // Dữ liệu giả cho các người dùng
//                UserRow(name = "Xuân Quỳnh", avatarResId = R.drawable.ava1, date = "12/04/2025")
//                UserRow(name = "Đức Triển", avatarResId = R.drawable.ava2, date = "10/04/2025")
//                UserRow(name = "Phú Quý", avatarResId = R.drawable.ava3, date = "29/03/3025")
//                UserRow(name = "Ngọc Huy", avatarResId = R.drawable.ava4, date = "12/04/2025")
//                UserRow(name = "Kim Chi", avatarResId = R.drawable.ava5, date = "18/02/2025")
//                UserRow(name = "Minh Nguyễn", avatarResId = R.drawable.ava6, date = "10/11/2024")
//            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
//                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(users) { user ->
                    UserRow(user)
                }
            }

        }

    }
}

@Preview(widthDp = 375, heightDp = 812)
@Composable
private fun ManageUser1Preview() {
    ManageUser1Screen(Modifier)

}

@Composable
fun UserRow(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 65.dp)
            .padding(bottom = 10.dp, start = 4.dp, end = 4.dp),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=12.dp,end = 12.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val avatarBitmap: ImageBitmap? = user.avatar
                .takeIf { it.isNotBlank() }
                ?.let { base64 ->
                    try {
                        val bytes = Base64.decode(base64, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            ?.asImageBitmap()                 // → ImageBitmap?
                    } catch (e: Exception) {
                        null
                    }
                }

            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap,                   // OK: non-null
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_default_avatar),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            }


            Text(user.username, modifier = Modifier.padding(start = 8.dp))
            Spacer(Modifier.weight(1f))

            val tag = if (user.premium) "Premium" else "Free"
            val color = if (user.premium) Color(0xFFFA6F3E) else Color.Gray

            Text(tag, color = color, fontWeight = FontWeight.SemiBold)

        }
    }
}