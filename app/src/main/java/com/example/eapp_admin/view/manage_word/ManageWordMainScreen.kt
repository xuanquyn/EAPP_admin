package com.example.eapp_admin.view.manage_word

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eapp_admin.R
import com.example.eapp_admin.data.model.VocabSet
import com.example.eapp_admin.view.AppTypes
import com.example.eapp_admin.viewmodel.VocabSetViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageWordMainScreen(
    modifier: Modifier = Modifier,
    viewModel: VocabSetViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val vocabSets by viewModel.vocabSets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterOption by viewModel.filterOption.collectAsState()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var showFilterDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadAllVocabSets()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF2F1EB))
    ) {
        // Header
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .padding(top = 23.dp)
                .fillMaxWidth()
                .height(40.dp)
        ) {
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
                    .align(alignment = Alignment.TopCenter)
                    .padding(top = 10.dp)
            )
            
            // Add button
            FloatingActionButton(
                onClick = {
                    viewModel.clear()
                    navController.navigate("createVocabSet")
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp)
                    .size(40.dp),
                containerColor = Color(0xFF4285F4),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add vocabulary set"
                )
            }
        }

        // Search bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .padding(top = 89.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(18.dp))
                .background(color = Color(0xFFF6F6F6))
                .border(border = BorderStroke(1.dp, Color(0xFFBDBDC4)),
                    shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image1),
                contentDescription = "Search icon",
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
            
            BasicTextField(
                value = searchText,
                onValueChange = { 
                    searchText = it
                    viewModel.updateSearchQuery(it.text)
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp
                ),
                decorationBox = { innerTextField ->
                    if (searchText.text.isEmpty()) {
                        Text(
                            text = "Tìm kiếm",
                            color = Color(0xFFB9BBC0),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .weight(1f)
            )
        }

        // Filter dropdown
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .padding(top = 141.dp, end = 24.dp)
                .width(92.dp)                .height(27.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(border = BorderStroke(1.dp, Color(0xFFAAAAAA)),
                    shape = RoundedCornerShape(10.dp))
                .clickable { showFilterDropdown = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {                Text(
                    text = when(filterOption) {
                        "newest" -> "Mới nhất"
                        "free" -> "Miễn phí"
                        "premium" -> "Premium"
                        else -> "Mới nhất"
                    },
                    color = Color(0xFF8B8A8A),
                    fontSize = 12.sp
                )
                
                Image(
                    painter = painterResource(id = R.drawable.group48096366),
                    contentDescription = "Dropdown arrow",
                    modifier = Modifier
                        .size(12.dp)
                )
            }
            
            DropdownMenu(
                expanded = showFilterDropdown,
                onDismissRequest = { showFilterDropdown = false },
                modifier = Modifier
                    .width(92.dp)
                    .background(Color.White)
            ) {
                DropdownMenuItem(
                    onClick = {
                        viewModel.updateFilterOption("newest")
                        showFilterDropdown = false
                    }
                ) {
                    Text("Mới nhất", fontSize = 12.sp)
                }
                  DropdownMenuItem(
                    onClick = {
                        viewModel.updateFilterOption("free")
                        showFilterDropdown = false
                    }
                ) {
                    Text("Miễn phí", fontSize = 12.sp)
                }
                
                DropdownMenuItem(
                    onClick = {
                        viewModel.updateFilterOption("premium")
                        showFilterDropdown = false
                    }
                ) {
                    Text("Premium", fontSize = 12.sp)
                }
            }
        }

        // Vocab set list
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF4285F4)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 180.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
            ) {
                items(vocabSets) { vocabSet ->
                    VocabSetItem(
                        vocabSet = vocabSet,
                        onClick = {
                            navController.navigate("vocabSetDetail/${vocabSet.vocabSetId}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun VocabSetItem(
    vocabSet: VocabSet,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(87.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = Color(0xFFF6F6F6))
            .border(
                border = BorderStroke(1.dp, Color(0xFFCAC8C8)),
                shape = RoundedCornerShape(15.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 18.dp, top = 8.dp, end = 18.dp, bottom = 8.dp)
        ) {
            // Title
            Text(
                text = vocabSet.vocabSetName,
                color = Color.Black,
                lineHeight = 1.43.em,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            )
            
            // Term count
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(68.dp)
                    .height(17.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xFFE2E2E2))
                        .align(Alignment.Center)
                )
                
                Text(
                    text = "${vocabSet.terms.size} từ vựng",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 8.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 4.dp)
                )
            }
            
            // Creator info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "Creator avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )
                
                Text(
                    text = if (vocabSet.created_by == "admin") "Admin" else "User",
                    color = Color(0xFF343333),
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                
                // Premium badge if applicable
                if (vocabSet.premiumContent) {
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700))
                            .padding(2.dp)
                    ) {
                        Text(
                            text = "P",
                            color = Color.Black,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageWordMainScreenPreview() {
    ManageWordMainScreen(
        viewModel = VocabSetViewModel(),
        navController = rememberNavController()
    )
}

