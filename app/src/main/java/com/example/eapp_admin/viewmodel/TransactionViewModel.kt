package com.example.eapp_admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eapp_admin.data.model.TransactionUI
import com.example.eapp_admin.data.repository.TransactionRepository
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TransactionViewModel(private val repo: TransactionRepository) : ViewModel() {
    // Sử dụng MutableStateFlow để theo dõi năm hiện tại
    private val _currentYear = MutableStateFlow(2025)
    val currentYear: StateFlow<Int> = _currentYear.asStateFlow()

    // StateFlow cho dữ liệu biểu đồ
    private val _monthlyRevenue = MutableStateFlow<List<Entry>>(emptyList())
    val monthlyRevenue: StateFlow<List<Entry>> = _monthlyRevenue

    init {
        // Tải dữ liệu cho năm mặc định khi khởi tạo
        loadRevenueDataForYear(2025)
    }

    // Hàm để thay đổi năm hiển thị
    fun setYear(year: Int) {
        _currentYear.value = year
        loadRevenueDataForYear(year)
    }

    // Hàm để tải dữ liệu cho năm cụ thể
    private fun loadRevenueDataForYear(year: Int) {
        viewModelScope.launch {
            getMonthlyRevenueFlow(year)
                .distinctUntilChanged()
                .collect { entries ->
                    _monthlyRevenue.value = entries
                }
        }
    }
    fun getMonthlyRevenueFlow(year: Int): Flow<List<Entry>> = callbackFlow {
        val db = FirebaseFirestore.getInstance()

        // Tính millis bắt đầu và kết thúc năm
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, year)
            set(java.util.Calendar.MONTH, 0)
            set(java.util.Calendar.DAY_OF_MONTH, 1)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val startOfYear = calendar.timeInMillis

        calendar.add(java.util.Calendar.YEAR, 1)
        val startOfNextYear = calendar.timeInMillis

        val listenerRegistration = db.collection("transactions")
            .whereGreaterThanOrEqualTo("timestamp", startOfYear)
            .whereLessThan("timestamp", startOfNextYear)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot == null) return@addSnapshotListener

                // Đếm số lần thanh toán theo tháng
                val monthCountMap = mutableMapOf<Int, Int>()
                for (doc in snapshot.documents) {
                    val timestamp = doc.getLong("timestamp") ?: continue
                    val cal = java.util.Calendar.getInstance().apply { timeInMillis = timestamp }
                    val month = cal.get(java.util.Calendar.MONTH) + 1 // tháng 1-12

                    monthCountMap[month] = monthCountMap.getOrDefault(month, 0) + 1
                }

                // Tạo List<Entry> cho biểu đồ (đảm bảo đủ 12 tháng)
                val entries = (1..12).map { month ->
                    // Tính doanh thu: số lần thanh toán * 0,599 và làm tròn 1 chữ số thập phân
                    val count = monthCountMap.getOrDefault(month, 0)
                    val revenue = (count * 0.599f).round(1) // Hàm làm tròn sẽ được định nghĩa bên dưới

                    // Tạo một instance Entry mới với constructor rõ ràng
                    val xValue = month.toFloat()
                    val yValue = revenue
                    Entry(xValue, yValue)
                }

                // In ra log để kiểm tra
                Log.d("RevenueViewModel", "Sending new revenue entries from Firestore: $entries")

                // Sử dụng toList() để tạo một bản sao mới của danh sách
                trySend(entries.toList())
            }

        awaitClose { listenerRegistration.remove() }
    }

    // Hàm mở rộng để làm tròn số thập phân theo số chữ số sau dấu phẩy
    fun Float.round(decimals: Int): Float {
        var multiplier = 1.0f
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }

    //Start lấy thông tin giao dịch chi tiết
    private val _items = MutableStateFlow<List<TransactionUI>>(emptyList())
    val items: StateFlow<List<TransactionUI>> = _items.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    init { load() }

    fun load() = viewModelScope.launch {
        _loading.value = true
        _items.value   = repo.getTransactions()
        _loading.value = false
    }
    //End lấy thông tin giao dịch chi tiết
}

class TxViewModelFactory(private val repo: TransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TransactionViewModel(repo) as T
}