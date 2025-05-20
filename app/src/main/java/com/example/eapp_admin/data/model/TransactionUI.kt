package com.example.eapp_admin.data.model

data class TransactionUI(
    val id:       String,
    val username: String,
    val avatarB64: String?,   // có thể null / rỗng
    val dateStr:  String
)
