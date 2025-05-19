package com.example.eapp_admin.view.manage_word

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.layout.ContentScale.Inside
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.eapp_admin.R
import com.example.eapp_admin.view.AppTypes
import com.example.eapp_admin.view.ManageRevenueScreen

@Composable
fun ManageWordMainScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 375.dp)
            .requiredHeight(height = 812.dp)
            .background(color = Color(0xfff2f1eb))
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .offset(x = 0.dp,
                    y = 23.dp)
                .requiredWidth(width = 375.dp)
                .requiredHeight(height = 40.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.plusmath),
                contentDescription = "Plus Math",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 337.dp,
                        y = 8.dp)
                    .requiredSize(size = 50.dp))
            Text(
                text = "Quản lí từ vựng",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.18.em,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 120.dp,
                        y = 10.dp))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp,
                    y = 89.dp)
                .requiredWidth(width = 327.dp)
                .clip(shape = RoundedCornerShape(18.dp))
                .background(color = Color(0xfff6f6f6))
                .border(border = BorderStroke(1.dp, Color(0xffbdbdc4)),
                    shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp,
                    vertical = 12.dp)
        ) {
            Text(
                text = "     Tìm kiếm",
                color = Color(0xffb9bbc0),
                lineHeight = 1.43.em,
                style = AppTypes.type_Body_14_Regular,
                modifier = Modifier
                    .requiredWidth(width = 225.dp))
            Image(
                painter = painterResource(id = R.drawable.image1),
                contentDescription = "image 1",
                modifier = Modifier
                    .requiredSize(size = 12.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 259.dp,
                    y = 141.dp)
                .requiredWidth(width = 92.dp)
                .requiredHeight(height = 27.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 92.dp)
                    .requiredHeight(height = 27.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(border = BorderStroke(1.dp, Color(0xffaaaeb0)),
                        shape = RoundedCornerShape(10.dp)))
            Text(
                text = "Mới nhất",
                color = Color(0xff8b8a8a),
                lineHeight = 1.67.em,
                style = TextStyle(
                    fontSize = 12.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 10.51.dp,
                        y = 4.dp)
                    .requiredWidth(width = 74.dp))
            Image(
                painter = painterResource(id = R.drawable.group48096366),
                contentDescription = "Group 48096366",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 69.66.dp,
                        y = 7.dp)
                    .requiredWidth(width = 12.dp)
                    .requiredHeight(height = 14.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 184.dp)
                .requiredWidth(width = 327.dp)
                .requiredHeight(height = 87.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 327.dp)
                    .requiredHeight(height = 87.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color(0xfff6f6f6))
                    .border(border = BorderStroke(1.dp, Color(0xffcac8c8)),
                        shape = RoundedCornerShape(15.dp)))
            Text(
                text = "B1 Mastery 1",
                color = Color.Black,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 8.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 15.dp,
                        y = 28.dp)
                    .requiredWidth(width = 68.dp)
                    .requiredHeight(height = 17.dp)
            ) {
                Text(
                    text = "35 từ vựng",
                    color = Color.Black.copy(alpha = 0.7f),
                    lineHeight = 2.5.em,
                    style = TextStyle(
                        fontSize = 8.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 10.dp,
                            y = 0.dp)
                        .requiredWidth(width = 58.dp)
                        .requiredHeight(height = 13.dp))
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 2.dp)
                        .requiredWidth(width = 64.dp)
                        .requiredHeight(height = 15.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xffe2e2e2)))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 55.dp)
                    .requiredWidth(width = 57.dp)
                    .requiredHeight(height = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image",
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                        .clip(shape = CircleShape))
                Text(
                    text = "Admin",
                    color = Color(0xff343333),
                    lineHeight = 2.em,
                    style = TextStyle(
                        fontSize = 10.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 23.dp,
                            y = 0.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp,
                    y = 282.dp)
                .requiredWidth(width = 327.dp)
                .requiredHeight(height = 87.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 327.dp)
                    .requiredHeight(height = 87.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color(0xfff6f6f6))
                    .border(border = BorderStroke(1.dp, Color(0xffcac8c8)),
                        shape = RoundedCornerShape(15.dp)))
            Text(
                text = "B1 Mastery 1",
                color = Color.Black,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 8.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 15.dp,
                        y = 28.dp)
                    .requiredWidth(width = 68.dp)
                    .requiredHeight(height = 17.dp)
            ) {
                Text(
                    text = "35 từ vựng",
                    color = Color.Black.copy(alpha = 0.7f),
                    lineHeight = 2.5.em,
                    style = TextStyle(
                        fontSize = 8.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 10.dp,
                            y = 0.dp)
                        .requiredWidth(width = 58.dp)
                        .requiredHeight(height = 13.dp))
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 2.dp)
                        .requiredWidth(width = 64.dp)
                        .requiredHeight(height = 15.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xffe2e2e2)))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 55.dp)
                    .requiredWidth(width = 57.dp)
                    .requiredHeight(height = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image",
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                        .clip(shape = CircleShape))
                Text(
                    text = "Admin",
                    color = Color(0xff343333),
                    lineHeight = 2.em,
                    style = TextStyle(
                        fontSize = 10.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 23.dp,
                            y = 0.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 22.dp,
                    y = 380.dp)
                .requiredWidth(width = 327.dp)
                .requiredHeight(height = 87.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 327.dp)
                    .requiredHeight(height = 87.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color(0xfff6f6f6))
                    .border(border = BorderStroke(1.dp, Color(0xffcac8c8)),
                        shape = RoundedCornerShape(15.dp)))
            Text(
                text = "B1 Mastery 1",
                color = Color.Black,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 8.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 15.dp,
                        y = 28.dp)
                    .requiredWidth(width = 68.dp)
                    .requiredHeight(height = 17.dp)
            ) {
                Text(
                    text = "35 từ vựng",
                    color = Color.Black.copy(alpha = 0.7f),
                    lineHeight = 2.5.em,
                    style = TextStyle(
                        fontSize = 8.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 10.dp,
                            y = 0.dp)
                        .requiredWidth(width = 58.dp)
                        .requiredHeight(height = 13.dp))
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 2.dp)
                        .requiredWidth(width = 64.dp)
                        .requiredHeight(height = 15.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xffe2e2e2)))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 55.dp)
                    .requiredWidth(width = 57.dp)
                    .requiredHeight(height = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image",
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                        .clip(shape = CircleShape))
                Text(
                    text = "Admin",
                    color = Color(0xff343333),
                    lineHeight = 2.em,
                    style = TextStyle(
                        fontSize = 10.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 23.dp,
                            y = 0.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 20.dp,
                    y = 478.dp)
                .requiredWidth(width = 327.dp)
                .requiredHeight(height = 87.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 327.dp)
                    .requiredHeight(height = 87.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color(0xfff6f6f6))
                    .border(border = BorderStroke(1.dp, Color(0xffcac8c8)),
                        shape = RoundedCornerShape(15.dp)))
            Text(
                text = "B1 Mastery 1",
                color = Color.Black,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 8.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 15.dp,
                        y = 28.dp)
                    .requiredWidth(width = 68.dp)
                    .requiredHeight(height = 17.dp)
            ) {

                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 2.dp)
                        .requiredWidth(width = 64.dp)
                        .requiredHeight(height = 15.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xffe2e2e2)))
                Text(
                    text = "35 từ vựng",
                    color = Color.Black.copy(alpha = 0.7f),
                    lineHeight = 2.5.em,
                    style = TextStyle(
                        fontSize = 8.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 10.dp,
                            y = 0.dp)
                        .requiredWidth(width = 58.dp)
                        .requiredHeight(height = 13.dp))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 55.dp)
                    .requiredWidth(width = 57.dp)
                    .requiredHeight(height = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image",
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                        .clip(shape = CircleShape))
                Text(
                    text = "Admin",
                    color = Color(0xff343333),
                    lineHeight = 2.em,
                    style = TextStyle(
                        fontSize = 10.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 23.dp,
                            y = 0.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 18.dp,
                    y = 576.dp)
                .requiredWidth(width = 327.dp)
                .requiredHeight(height = 87.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 327.dp)
                    .requiredHeight(height = 87.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color(0xfff6f6f6))
                    .border(border = BorderStroke(1.dp, Color(0xffcac8c8)),
                        shape = RoundedCornerShape(15.dp)))
            Text(
                text = "B1 Mastery 1",
                color = Color.Black,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 8.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 15.dp,
                        y = 28.dp)
                    .requiredWidth(width = 68.dp)
                    .requiredHeight(height = 17.dp)
            ) {
                Text(
                    text = "35 từ vựng",
                    color = Color.Black.copy(alpha = 0.7f),
                    lineHeight = 2.5.em,
                    style = TextStyle(
                        fontSize = 8.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 10.dp,
                            y = 0.dp)
                        .requiredWidth(width = 58.dp)
                        .requiredHeight(height = 13.dp))
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 2.dp)
                        .requiredWidth(width = 64.dp)
                        .requiredHeight(height = 15.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xffe2e2e2)))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 18.dp,
                        y = 55.dp)
                    .requiredWidth(width = 57.dp)
                    .requiredHeight(height = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image",
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                        .clip(shape = CircleShape))
                Text(
                    text = "Admin",
                    color = Color(0xff343333),
                    lineHeight = 2.em,
                    style = TextStyle(
                        fontSize = 10.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 23.dp,
                            y = 0.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
    }
}

@Preview(widthDp = 375, heightDp = 812)
@Composable
private fun ManageWordMainPreview() {
    ManageWordMainScreen(Modifier)
}

