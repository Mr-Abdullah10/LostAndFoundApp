package com.abdullah.lostfound.ui.main.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.lostfound.Repositories.LostRepository
import com.abdullah.lostfound.Repositories.StorageRepository
import com.abdullah.lostfound.ui.dataclasses.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LostViewModel: ViewModel() {
    val lostsRepository = LostRepository()
    val storageRepository = StorageRepository()

    val isSuccessfullySaved = MutableStateFlow<Boolean?>(null)
    val failureMessage = MutableStateFlow<String?>(null)

    fun uploadImageAndSaveLost(imagePath: String, lost: Lost) {
        storageRepository.uploadFile(imagePath, onComplete = { success, result ->
            if (success) {
                lost.image=result!!
                saveLost(lost)
            }
            else failureMessage.value=result
        })
    }
    fun saveLost(lost: Lost) {
        viewModelScope.launch {
            val result = lostsRepository.saveLost(lost)
            if (result.isSuccess) {
                isSuccessfullySaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

}
