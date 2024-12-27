package com.abdullah.lostfound.Repositories



import com.abdullah.lostfound.ui.dataclasses.Lost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class LostRepository {
    val lostCollection = FirebaseFirestore.getInstance().collection("losts")


    suspend fun saveLost(lost: Lost): Result<Boolean> {
        try {
            val document = lostCollection.document()
            lost.id = document.id
            document.set(lost).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)

        }
    }
    suspend fun updateLost(lost: Lost): Result<Boolean> {
        try {
            val document = lostCollection.document(lost.id)
            document.set(lost).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    fun getLosts() =
        lostCollection.whereEqualTo("status", "Pending").snapshots().map { it.toObjects(Lost::class.java) }

    fun getFoundItem() =
        lostCollection.whereEqualTo("found", true).snapshots().map { it.toObjects(Lost::class.java) }

    fun getLostItem() =
        lostCollection.whereEqualTo("lost", true).snapshots().map { it.toObjects(Lost::class.java) }

    fun completed() = lostCollection
        .whereIn("status", listOf("Delivered", "Item Claimed"))
        .snapshots()
        .map { it.toObjects(Lost::class.java) }



}