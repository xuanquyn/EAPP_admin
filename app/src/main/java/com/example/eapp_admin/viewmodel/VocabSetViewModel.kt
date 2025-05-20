package com.example.eapp_admin.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eapp_admin.data.model.Term
import com.example.eapp_admin.data.model.VocabSet
import com.example.eapp_admin.data.repository.VocabSetRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VocabSetViewModel(
    private val repository: VocabSetRepository = VocabSetRepository()
) : ViewModel() {

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
            
    // For editing a vocab set    var vocabSetId by mutableStateOf<String?>(null)
    var vocabSetId by mutableStateOf("") //here
    var vocabSetName by mutableStateOf("")
    var isPublic by mutableStateOf(false)
    var created_by by mutableStateOf("")
    var terms = mutableStateListOf(Term(), Term())
    var premiumContent by mutableStateOf(false)
    
    // For listing vocab sets
    private val _vocabSets = MutableStateFlow<List<VocabSet>>(emptyList())
    val vocabSets: StateFlow<List<VocabSet>> = _vocabSets.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _filterOption = MutableStateFlow("newest")
    val filterOption: StateFlow<String> = _filterOption.asStateFlow()
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            searchVocabSets(query)
        } else {
            loadAllVocabSets()
        }
    }
    
    fun updateFilterOption(option: String) {
        _filterOption.value = option
        applyFilter(option)
    }
      private fun applyFilter(option: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val sets = when (option) {
                    "newest" -> repository.getAllVocabSets()
                    "free" -> repository.getFreeVocabSets()
                    "premium" -> repository.getPremiumVocabSets()
                    else -> repository.getAllVocabSets()
                }
                _vocabSets.value = sets
            } catch (e: Exception) {
                Log.e("VocabSetViewModel", "Error applying filter: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadAllVocabSets() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val sets = repository.getAllVocabSets()
                _vocabSets.value = sets
            } catch (e: Exception) {
                Log.e("VocabSetViewModel", "Error loading vocab sets: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun searchVocabSets(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val sets = repository.getVocabSetsByName(query)
                _vocabSets.value = sets
            } catch (e: Exception) {
                Log.e("VocabSetViewModel", "Error searching vocab sets: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
      fun loadVocabSetById(id: String) {
        repository.getVocabSetById(id) { vocabSet ->
            vocabSetId = vocabSet.vocabSetId
            vocabSetName = vocabSet.vocabSetName
            isPublic = vocabSet._public
            premiumContent = vocabSet.premiumContent
            created_by = vocabSet.created_by
            terms = vocabSet.terms.toMutableStateList()
        }
    }
    
    fun addTermField() {
        terms.add(Term())
    }
    
    fun removeTerm(index: Int) {
        if (index in terms.indices) {
            terms.removeAt(index)
        }
    }
    
    fun updateTerm(index: Int, newTerm: String) {
        terms[index] = terms[index].copy(term = newTerm)
    }
    
    fun updateDefinition(index: Int, newDefinition: String) {
        terms[index] = terms[index].copy(definition = newDefinition)
    }
    
    fun togglePrivacy() {
        isPublic = !isPublic
    }
    
    fun togglePremium() {
        premiumContent = !premiumContent
    }
      fun clear() {
        vocabSetId = ""
        vocabSetName = ""
        isPublic = false
        created_by = ""
        premiumContent = false
        terms = mutableStateListOf(Term())
    }
      fun saveVocabSet(
        adminId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val vocabSet = VocabSet(
            vocabSetId = vocabSetId ?: "",
            vocabSetName = vocabSetName,
            _public = isPublic,
            created_by = adminId,
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis(),
            terms = terms,
            premiumContent = premiumContent
        )
        repository.addVocabSet(vocabSet, onSuccess, onFailure)
    }
      fun updateVocabSet(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (vocabSetId.isNullOrEmpty()) {
            onFailure(Exception("Invalid vocab set ID"))
            return
        }
        
        val vocabSet = VocabSet(
            vocabSetId = vocabSetId ?: "",
            vocabSetName = vocabSetName,
            _public = isPublic,
            created_by = created_by,
            terms = terms,
            updated_at = System.currentTimeMillis(),
            premiumContent = premiumContent
        )
        repository.updateVocabSet(vocabSet, onSuccess, onFailure)
    }
      fun deleteVocabSet(
        onSuccess: () -> Unit, 
        onFailure: (Exception) -> Unit
    ) {
        vocabSetId?.let { id ->
            repository.deleteVocabSet(id, onSuccess, onFailure)
        } ?: onFailure(Exception("Invalid vocab set ID"))
    }
    
    fun setPremiumStatus(
        isPremium: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        vocabSetId?.let { id ->
            repository.setPremiumStatus(id, isPremium, onSuccess, onFailure)
        } ?: onFailure(Exception("Invalid vocab set ID"))
    }
}
