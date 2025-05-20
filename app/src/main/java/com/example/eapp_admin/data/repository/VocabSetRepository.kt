package com.example.eapp_admin.data.repository

import android.util.Log
import com.example.eapp_admin.data.model.VocabSet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class VocabSetRepository {

    private val firestore = FirebaseFirestore.getInstance()
    val collection = firestore.collection("vocab_sets")

    // Thêm reference đến users collection
    private val usersCollection = FirebaseFirestore
        .getInstance()
        .collection("users")


    fun addVocabSet(
        vocabSet: VocabSet,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val doc = collection.document(vocabSet.vocabSetId.ifEmpty { collection.document().id })
        val vocabSetWithId = vocabSet.copy(vocabSetId = doc.id)

        doc.set(vocabSetWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateVocabSet(
        vocabSet: VocabSet,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val id = vocabSet.vocabSetId
        collection.document(id)
            .set(vocabSet)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteVocabSet(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun getVocabSetById(
        id: String,
        onResult: (VocabSet) -> Unit
    ) {
        collection.document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val vocabSet = document.toObject(VocabSet::class.java)
                    vocabSet?.vocabSetId = document.id
                    if (vocabSet != null) {
                        onResult(vocabSet)
                    }
                }
            }
    }

    suspend fun getAllVocabSets(limit: Long = 50): List<VocabSet> {
        return try {
            val snapshot = collection
                .orderBy("updated_at", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            val adminSets = mutableListOf<VocabSet>()
            for (doc in snapshot.documents) {
                try {
                    // Lấy đối tượng VocabSet
                    val vs = doc.toObject(VocabSet::class.java)
                        ?.apply { vocabSetId = doc.id }
                        ?: continue

                    // Lấy created_by để fetch user
                    val createdById = doc.getString("created_by")
                        ?: continue

                    // Lấy user document
                    val userDoc = usersCollection
                        .document(createdById)
                        .get()
                        .await()
                    val role = userDoc.getString("role")

                    // Nếu là ADMIN thì add vào danh sách
                    if (role == "ADMIN") {
                        adminSets += vs
                    }
                } catch (e: Exception) {
                    Log.e("VocabSetRepository", "Lỗi xử lý ${doc.id}: $e")
                }
            }
            adminSets
        } catch (e: Exception) {
            Log.e("VocabSetRepository", "Error fetching vocab sets: $e")
            emptyList()
        }
    }

//    suspend fun getAllVocabSets(limit: Long = 50): List<VocabSet> {
//        return try {
//            val snapshot = collection
//                .orderBy("updated_at", Query.Direction.DESCENDING)
//                .limit(limit)
//                .get()
//                .await()
//            snapshot.documents.mapNotNull { doc ->
//                try {
//                    doc.toObject(VocabSet::class.java)?.apply { vocabSetId = doc.id }
//                } catch (e: Exception) {
//                    Log.e("VocabSetRepository", "Error mapping VocabSet ${doc.id}: $e")
//                    null
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("VocabSetRepository", "Error fetching vocab sets: $e")
//            emptyList()
//        }
//    }

    suspend fun getVocabSetsByName(searchQuery: String, limit: Long = 50): List<VocabSet> {
        return try {
            val snapshot = collection
                .orderBy("vocabSetName")
                .startAt(searchQuery)
                .endAt(searchQuery + "\uf8ff")
                .limit(limit)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(VocabSet::class.java)?.apply { vocabSetId = doc.id }
                } catch (e: Exception) {
                    Log.e("VocabSetRepository", "Error mapping VocabSet ${doc.id}: $e")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("VocabSetRepository", "Error searching vocab sets: $e")
            emptyList()
        }
    }

    suspend fun getPublicVocabSets(limit: Long = 50): List<VocabSet> {
        return try {
            val snapshot = collection
                .whereEqualTo("_public", true)
                .orderBy("updated_at", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(VocabSet::class.java)?.apply { vocabSetId = doc.id }
                } catch (e: Exception) {
                    Log.e("VocabSetRepository", "Error mapping VocabSet ${doc.id}: $e")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("VocabSetRepository", "Error fetching public vocab sets: $e")
            emptyList()
        }    }

    suspend fun getFreeVocabSets(limit: Long = 50): List<VocabSet> {
        return try {
            // 1️⃣ Lấy các vocab set free
            val snapshot = collection
                .whereEqualTo("premiumContent", false)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            val result = mutableListOf<VocabSet>()

            // 2️⃣ Với mỗi bộ, kiểm tra xem creator có phải ADMIN không
            for (doc in snapshot.documents) {
                try {
                    val vs = doc.toObject(VocabSet::class.java)
                        ?.apply { vocabSetId = doc.id }
                        ?: continue

                    val createdById = doc.getString("created_by") ?: continue

                    val userDoc = usersCollection
                        .document(createdById)
                        .get()
                        .await()
                    val role = userDoc.getString("role")

                    if (role == "ADMIN") {
                        result += vs
                    }
                } catch (e: Exception) {
                    Log.e("VocabSetRepository", "Error processing doc ${doc.id}: $e")
                }
            }

            result
        } catch (e: Exception) {
            Log.e("VocabSetRepository", "Error fetching free vocab sets: $e")
            emptyList()
        }
    }

    suspend fun getPremiumVocabSets(limit: Long = 50): List<VocabSet> {
        return try {
            val snapshot = collection
                .whereEqualTo("premiumContent", true)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            Log.e("VocabSetRepository", "done")
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(VocabSet::class.java)?.apply { vocabSetId = doc.id }
                } catch (e: Exception) {
                    Log.e("VocabSetRepository", "Error mapping VocabSet ${doc.id}: $e")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("VocabSetRepository", "Error fetching premium vocab sets: $e")
            emptyList()
        }    }

    // Admin-specific functions
    fun setPremiumStatus(
        id: String,
        isPremium: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id)
            .update("premiumContent", isPremium)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
