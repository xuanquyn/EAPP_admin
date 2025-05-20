package com.example.eapp_admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import com.example.eapp_admin.view.LoginScreen
import com.example.eapp_admin.view.DashboardScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.eapp_admin.view.ManageRevenueScreen
import com.example.eapp_admin.view.bottomNav.BottomNavItem
import com.example.eapp_admin.view.bottomNav.BottomNavigationBar
import com.example.eapp_admin.view.manage_user.ManageUser1Screen
import com.example.eapp_admin.view.manage_word.ManageWordMainScreen
import com.example.eapp_admin.view.manage_word.CreateVocabSetScreen
import com.example.eapp_admin.view.manage_word.VocabSetDetailScreen
import com.example.eapp_admin.view.manage_word.EditVocabSetScreen
import com.example.eapp_admin.view.theme.EAPP_adminTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.example.eapp_admin.viewmodel.AuthViewModel
import com.example.eapp_admin.viewmodel.VocabSetViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    // Đăng ký launcher trong onCreate
    private lateinit var storagePermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Đăng ký trước khi setContent
        storagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { /* callback nếu cần */ }

//        requestPermissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//        }

        enableEdgeToEdge()
        setContent {
            EAPP_adminTheme {
                MyApp(storagePermissionLauncher)
            }
        }
    }
}

@Composable
fun MyApp(storageLauncher: ActivityResultLauncher<Array<String>>) {
    val navController = rememberNavController()

    // Tạo AuthViewModel không cần factory (nếu bạn tạo đúng không tham số)
    val authViewModel: AuthViewModel = viewModel()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute != "login") {
                val currentRouteNonNull = currentRoute ?: BottomNavItem.Dashboard.route
                BottomNavigationBar(currentRoute = currentRouteNonNull, onTabSelected = { item ->
                    if (currentRouteNonNull != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                    }
                })
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(
                    modifier = Modifier,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }            composable(BottomNavItem.Dashboard.route) { DashboardScreen() }
            composable(BottomNavItem.ManageWord.route) { 
                val vocabSetViewModel: VocabSetViewModel = viewModel()
                ManageWordMainScreen(
                    viewModel = vocabSetViewModel,
                    navController = navController
                ) 
            }
            composable(BottomNavItem.ManageUser.route) { ManageUser1Screen() }
            composable(BottomNavItem.ManageRevenue.route) {                ManageRevenueScreen(
                    requestPermissionLauncher = storageLauncher    // ← quan trọng
                )
            }
            
            // Vocabulary management routes
            composable("createVocabSet") {
                CreateVocabSetScreen(
                    viewModel = viewModel(),
                    navController = navController
                )
            }
            composable("vocabSetDetail/{vocabSetId}") { backStackEntry ->
                val vocabSetId = backStackEntry.arguments?.getString("vocabSetId") ?: ""
                VocabSetDetailScreen(
                    vocabSetId = vocabSetId,
                    viewModel = viewModel(),
                    navController = navController
                )
            }
            composable("editVocabSet/{vocabSetId}") { backStackEntry ->
                val vocabSetId = backStackEntry.arguments?.getString("vocabSetId") ?: ""
                EditVocabSetScreen(
                    vocabSetId = vocabSetId,
                    viewModel = viewModel(),
                    navController = navController
                )
            }
//            composable(BottomNavItem.ManageRevenue.route) { ManageRevenueScreen() }
        }
    }
}

//class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
//
//    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
//        viewModelScope.launch {
//            val result = userRepository.login(email, password)
//            if (result.isSuccess) {
//                onResult(true, result.getOrNull()) // token
//            } else {
//                onResult(false, result.exceptionOrNull()?.message)
//            }
//        }
//    }
//}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EAPP_adminTheme {
    }
}

//@Composable
//fun MyApp() {
//    ManageRevenueScreen(Modifier)
//    DashboardScreen(Modifier)
//    ManageUser1Screen(Modifier)
//    ManageWordMainScreen(Modifier)
//    LoginScreen(Modifier)
//}