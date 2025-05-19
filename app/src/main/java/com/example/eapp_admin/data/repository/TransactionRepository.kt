package com.example.eapp_admin.data.repository

import com.example.eapp_admin.data.model.Transaction
import com.example.eapp_admin.data.model.TransactionUI
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getTransactions(): List<TransactionUI> {
        /* map userId → Pair(username, avatar) */
        val usersMap = db.collection("users").get().await()
            .documents.associate { doc ->
                doc.id to Pair(
                    doc.getString("username") ?: "Unknown",
                    doc.getString("avatar")   ?: ""          // Base64
                )
            }

        val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return db.collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
            .documents.map { d ->
                val uid = d.getString("userId") ?: ""
                val (uname, avatar) = usersMap[uid] ?: ("Unknown" to "")
                TransactionUI(
                    id        = d.id,
                    username  = uname,
                    avatarB64 = avatar,
                    dateStr   = fmt.format(Date(d.getLong("timestamp") ?: 0L))
                )
            }
    }
}
