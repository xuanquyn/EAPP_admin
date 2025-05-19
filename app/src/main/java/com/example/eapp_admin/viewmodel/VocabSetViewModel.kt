package com.example.eapp_admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VocabSetViewModel : ViewModel() {

    /** Flow<Int> luôn phát ra tổng số document của collection vocab_sets */
    val totalSets: StateFlow<Int> =
        callbackFlow<Int> {
            val listener = Firebase.firestore.collection("vocab_sets")
                .addSnapshotListener { snap, err ->
                    if (err != null) {
                        // Có lỗi → gửi -1 để UI hiển thị dấu hiệu lỗi nếu muốn
                        trySend(-1)
                        return@addSnapshotListener
                    }
                    trySend(snap?.size() ?: 0)
                }
            awaitClose { listener.remove() }
        }
            .stateIn(                       // biến thành StateFlow để UI collect
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                0                            // giá trị khởi tạo
            )
}
