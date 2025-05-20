package com.example.eapp_admin.data.repository

import com.example.eapp_admin.data.model.User
import com.example.eapp_admin.data.model.UserRole
import com.example.eapp_admin.data.remote.AuthApi
import com.example.eapp_admin.data.remote.LoginRequest
import com.example.eapp_admin.data.remote.LoginResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import retrofit2.Response

class UserRepository() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

//    suspend fun login(email: String, password: String): Response<LoginResponse> {
//        return authApi.login(LoginRequest(email, password))
//    }

    suspend fun getUsers(): List<User> {
        /*  Firestore hỗ trợ whereNotEqualTo (>= v24) – nếu project của bạn
            chưa bật, bạn có thể dùng whereEqualTo("role", "USER")
            hoặc notIn(listOf("ADMIN")).
        */
        val snap = db.collection("users")
            .whereNotEqualTo("role", "ADMIN")  // loại ADMIN
            .get()
            .await()

        return snap.documents.map { doc ->
            User(
                userId    = doc.id,
                email     = doc.getString("email")    ?: "",
                username  = doc.getString("username") ?: "",
                avatar    = doc.getString("avatar")   ?: "",
                createdAt = doc.getLong("createdAt")  ?: 0L,
                premium   = doc.getBoolean("premium") ?: false,
                role      = when (doc.getString("role")) {
                    "ADMIN" -> UserRole.ADMIN
                    else    -> UserRole.USER
                },
                streak    = (doc.getLong("streak") ?: 0L).toInt()
            )
        }
    }
}


