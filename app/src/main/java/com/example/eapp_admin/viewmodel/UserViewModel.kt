package com.example.eapp_admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eapp_admin.data.model.User
import com.example.eapp_admin.data.repository.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.*
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val db = Firebase.firestore

    // Lấy tất cả User
    private fun getUserCountFlow(): Flow<Int> = callbackFlow {
        val listener = db.collection("users")
            .whereEqualTo("role", "USER")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0)
            }
        awaitClose { listener.remove() }
    }
    val userCountNum: StateFlow<Int> = getUserCountFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    //Lấy số lượng tài khoản Premium
    fun getPremiumUserCountFlow(): Flow<Int> = callbackFlow {
        val listener = Firebase.firestore.collection("users")
            .whereEqualTo("premium", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0)
            }
        awaitClose { listener.remove() }
    }
    val premiumUserCount: StateFlow<Int> = getPremiumUserCountFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    //lấy user hiển thị cho LineChart

    // Sử dụng MutableStateFlow để theo dõi năm hiện tại
    private val _currentYear = MutableStateFlow(2025)
    val currentYear: StateFlow<Int> = _currentYear.asStateFlow()
    // StateFlow cho dữ liệu biểu đồ
    private val _monthlyRegistrations = MutableStateFlow<List<Entry>>(emptyList())
    val monthlyRegistrations: StateFlow<List<Entry>> = _monthlyRegistrations
    init {
        // Tải dữ liệu cho năm mặc định khi khởi tạo
        loadUserDataForYear(2025)
    }

    // Hàm để thay đổi năm hiển thị
    fun setYear(year: Int) {
        _currentYear.value = year
        loadUserDataForYear(year)
    }

    // Hàm để tải dữ liệu cho năm cụ thể
    private fun loadUserDataForYear(year: Int) {
        viewModelScope.launch {
            getMonthlyUserRegistrationsFlow(year)
                .distinctUntilChanged()
                .collect { entries ->
                    _monthlyRegistrations.value = entries
                }
        }
    }
    fun getMonthlyUserRegistrationsFlow(year: Int): Flow<List<Entry>> = callbackFlow {
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

        val listenerRegistration = db.collection("users")
            .whereGreaterThanOrEqualTo("createdAt", startOfYear)
            .whereLessThan("createdAt", startOfNextYear)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot == null) return@addSnapshotListener

                // Đếm số user đăng ký mỗi tháng
                val monthCountMap = mutableMapOf<Int, Int>()
                for (doc in snapshot.documents) {
                    val createdAt = doc.getLong("createdAt") ?: continue
                    val cal = java.util.Calendar.getInstance().apply { timeInMillis = createdAt }
                    val month = cal.get(java.util.Calendar.MONTH) + 1 // tháng 1-12

                    monthCountMap[month] = monthCountMap.getOrDefault(month, 0) + 1
                }

                // Tạo List<Entry> cho biểu đồ (đảm bảo đủ 12 tháng) - THAY ĐỔI TẠI ĐÂY
                val entries = (1..12).map { month ->
                    // Tạo một instance Entry mới với constructor rõ ràng
                    val xValue = month.toFloat()
                    val yValue = monthCountMap.getOrDefault(month, 0).toFloat()
                    Entry(xValue, yValue)
                }

                // In ra log để kiểm tra
                Log.d("UserViewModel", "Sending new entries from Firestore: $entries")

                // Sử dụng toList() để tạo một bản sao mới của danh sách
                trySend(entries.toList())
            }
        awaitClose { listenerRegistration.remove() }
    }

    //Start lấy tất cả User
    private val _users   = MutableStateFlow<List<User>>(emptyList())
    private val _loading = MutableStateFlow(false)
    private val _error   = MutableStateFlow<String?>(null)

    val users:   StateFlow<List<User>> = _users.asStateFlow()
    val loading: StateFlow<Boolean>    = _loading.asStateFlow()
    val error:   StateFlow<String?>    = _error.asStateFlow()

    init { fetchUsers() }

    fun fetchUsers() = viewModelScope.launch {
        _loading.value = true
        _error.value   = null
        try {
            _users.value = repo.getUsers()          // suspend fun
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
        } finally {
            _loading.value = false
        }
    }
    //End lấy tất cả User
}

/* ---- Factory cho lấy tất cả User ---- */
class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
