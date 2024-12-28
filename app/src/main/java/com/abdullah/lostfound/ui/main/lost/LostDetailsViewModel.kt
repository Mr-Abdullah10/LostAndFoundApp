package com.abdullah.lostfound.ui.main.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.lostfound.Repositories.LostRepository
import com.abdullah.lostfound.ui.dataclasses.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LostDetailsViewModel : ViewModel() {

    private val lostRepository = LostRepository()

    // StateFlows to observe update and delete operations
    val isUpdated = MutableStateFlow<Boolean?>(null)
    val isDeleted = MutableStateFlow<Boolean?>(null)
    val failureMessage = MutableStateFlow<String?>(null)

    // Function to update the case
    fun updateLost(lost: Lost) {
        viewModelScope.launch {
            val result = lostRepository.updateLost(lost)
            if (result.isSuccess) {
                isUpdated.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    // Function to delete the case
    fun deleteCase(caseId: String) {
        viewModelScope.launch {
            try {
                val result = lostRepository.deleteCase(caseId)
                if (result.isSuccess) {
                    isDeleted.value = true // Deletion was successful
                } else {
                    failureMessage.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                failureMessage.value = e.message // Handle unexpected errors
            }
        }
    }
}
