@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.eapp_admin.view.manage_word

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eapp_admin.viewmodel.VocabSetViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVocabSetScreen(
    modifier: Modifier = Modifier,
    viewModel: VocabSetViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.clear()
    }
    
    var isPublic by remember { mutableStateOf(false) }
    var isPremium by remember { mutableStateOf(false) }
    var addingTerm by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF2F1EB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back",
                        tint = Color.Black
                    )
                }
                
                Text(
                    text = "Tạo bộ từ vựng mới",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                // Spacer for alignment
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Vocab set name
            OutlinedTextField(
                value = viewModel.vocabSetName,
                onValueChange = { viewModel.vocabSetName = it },
                label = { Text("Tên bộ từ vựng") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4285F4),
                    unfocusedBorderColor = Color(0xFFCAC8C8)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Privacy and Premium settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Public/Private toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.togglePrivacy() }
                ) {
                    Checkbox(
                        checked = viewModel.isPublic,
                        onCheckedChange = { viewModel.togglePrivacy() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF4285F4)
                        )
                    )
                    Text(
                        text = "Free",
                        fontSize = 16.sp
                    )
                }
                
                // Premium toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.togglePremium() }
                ) {
                    Checkbox(
                        checked = viewModel.premiumContent,
                        onCheckedChange = { viewModel.togglePremium() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFFFD700)
                        )
                    )
                    Text(
                        text = "Premium",
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Terms section
            Text(
                text = "Từ vựng",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Terms list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(viewModel.terms.size) { index ->
                    TermItem(
                        index = index,
                        term = viewModel.terms[index].term,
                        definition = viewModel.terms[index].definition,
                        onTermChange = { viewModel.updateTerm(index, it) },
                        onDefinitionChange = { viewModel.updateDefinition(index, it) },
                        onRemove = { viewModel.removeTerm(index) }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    // Add term button
                    Button(
                        onClick = { viewModel.addTermField() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE2E2E2),
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add term"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Thêm từ vựng")
                    }
                }
            }
            
            // Save button
            Button(
                onClick = {
                    val admin = FirebaseAuth.getInstance().currentUser
                    if (admin != null) {
                        viewModel.saveVocabSet(
                            adminId = admin.uid,
                            onSuccess = {
                                Toast.makeText(context, "Đã tạo bộ từ vựng mới", Toast.LENGTH_SHORT).show()
                                navController.navigateUp()
                            },
                            onFailure = { e ->
                                Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Bạn cần đăng nhập để thực hiện chức năng này", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4285F4)
                )
            ) {
                Text("Lưu bộ từ vựng")
            }
        }
    }
}

@Composable
fun TermItem(
    index: Int,
    term: String,
    definition: String,
    onTermChange: (String) -> Unit,
    onDefinitionChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color(0xFFCAC8C8)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Term header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Từ ${index + 1}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove term",
                        tint = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Term input
            OutlinedTextField(
                value = term,
                onValueChange = onTermChange,
                label = { Text("Từ vựng") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4285F4),
                    unfocusedBorderColor = Color(0xFFCAC8C8)
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Definition input
            OutlinedTextField(
                value = definition,
                onValueChange = onDefinitionChange,
                label = { Text("Định nghĩa") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4285F4),
                    unfocusedBorderColor = Color(0xFFCAC8C8)
                )
            )
        }
    }
}
