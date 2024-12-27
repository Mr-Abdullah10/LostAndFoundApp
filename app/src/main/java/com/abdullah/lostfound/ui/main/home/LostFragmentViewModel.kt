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
        readLostItem()
    }

    fun readLostItem() {
        viewModelScope.launch {
            lostRepository.getLostItem().catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }
}