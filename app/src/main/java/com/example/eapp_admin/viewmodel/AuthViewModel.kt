package com.example.eapp_admin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eapp_admin.data.repository.UserRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.eapp_admin.data.remote.LoginRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

//class AuthViewModel(
//    private val userRepository: UserRepository
//) : ViewModel() {
//
//    var isLoading by mutableStateOf(false)
//        private set
//
//    private fun isEmailValid(email: String): Boolean {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    }
//
//    suspend fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
//        try {
//            val response = userRepository.login(email, password)
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null && body.success) {
//                    onSuccess()
//                } else {
//                    onError(body?.message ?: "Đăng nhập thất bại")
//                }
//            } else {
//                onError("Lỗi server: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            onError(e.localizedMessage ?: "Lỗi kết nối")
//        }
//    }
//
//}

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    var isLoading by mutableStateOf(false)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Lỗi đăng nhập")
            } finally {
                isLoading = false
            }
        }
    }
}