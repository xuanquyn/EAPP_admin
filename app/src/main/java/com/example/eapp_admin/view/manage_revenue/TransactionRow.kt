package com.example.eapp_admin.view.manage_revenue

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.eapp_admin.R
import com.example.eapp_admin.data.model.TransactionUI

@Composable
fun TransactionRow(tx: TransactionUI, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(370.dp)
            .height(105.dp)
    ) {
        Box(
            Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        )
        val avatarBitmap: ImageBitmap? = tx.avatarB64          // String?
            ?.takeIf { it.isNotBlank() }                       // ⬅️ safe-call
            ?.let { base64 ->
                try {
                    val bytes = Base64.decode(base64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        ?.asImageBitmap()
                } catch (e: Exception) { null }
            }

        if (avatarBitmap != null) {
            Image(
                bitmap = avatarBitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(29.dp, 25.dp)
                    .align(Alignment.TopStart)
                    .offset(203.dp, 10.dp)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_default_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(29.dp, 25.dp)
                    .align(Alignment.TopStart)
                    .offset(203.dp, 10.dp)
            )
        }

        Text(
            text = "User",
            color = Color(0xff858585),
            lineHeight = 1.43.em,
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 16.dp,
                    y = 12.dp))
        Text(
            text = tx.username,
            color = Color.Black,
            textAlign = TextAlign.End,
            lineHeight = 1.43.em,
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 236.dp,
                    y = 12.dp))
        Divider(
            color = Color(0xffb9bbc0),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 16.dp,
                    y = 37.dp)
                .requiredWidth(width = 310.dp))
        Text(
            text = "Số tiền",
            color = Color(0xff858585),
            lineHeight = 1.43.em,
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 16.dp,
                    y = 42.dp))
        Text(
            text = "vnđ",
            color = Color.Black,
            textAlign = TextAlign.End,
            lineHeight = 1.43.em,
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 305.dp,
                    y = 42.dp))
        Text(
            text = "599,000",
            color = Color.Black,
            textAlign = TextAlign.End,
            lineHeight = 1.43.em,
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 251.dp,
                    y = 42.dp))
        Divider(
            color = Color(0xffb9bbc0),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 16.dp,
                    y = 67.dp)
                .requiredWidth(width = 310.dp))
        Text(
            text = "Ngày",
            color = Color(0xff858585),
            lineHeight = 1.43.em,
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 15.dp,
                    y = 72.dp))
        Text(
            text = tx.dateStr,
            color = Color.Black,
            textAlign = TextAlign.End,
            lineHeight = 1.43.em,
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 255.dp,
                    y = 72.dp))
    }
}
