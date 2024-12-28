package com.abdullah.lostfound.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.lostfound.Repositories.LostRepository
import com.abdullah.lostfound.ui.dataclasses.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LostFragmentViewModel: ViewModel() {

    val lostRepository = LostRepository()

    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Lost>?>(null)

    init {
        readLostItem(lost = true, status = "Pending")
    }


    fun readLostItem(lost: Boolean, status: String) {
        viewModelScope.launch {
            lostRepository.getLostItem(lost, status)
                .catch { exception ->
                    failureMessage.value = exception.message
                }
                .collect { itemList ->
                    data.value = itemList
                }
        }
    }

}